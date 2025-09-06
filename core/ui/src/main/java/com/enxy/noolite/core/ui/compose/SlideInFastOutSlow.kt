package com.enxy.noolite.core.ui.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackAnimatable
import com.arkivanov.essenty.backhandler.BackEvent
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.StackAnimator as ExperimentalStackAnimator
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimator as experimentalStackAnimator

private const val SlideInFastOutSlowBackFactor = 0.3f
private const val PredictiveBackMaxSwipeProgressFactor = 0.3f
private const val NavigationAnimationDuration = 300


/**
 * A slide animation that slides the new screen in fast, and the old screen out slowly.
 * When going back, the old screen slides in slowly, and the new screen slides out fast.
 *
 * @param animationSpec The animation spec to use.
 */
@OptIn(ExperimentalDecomposeApi::class)
fun slideInFastOutSlowExperimental(
    animationSpec: FiniteAnimationSpec<Float> = navigationAnimationSpec(),
): ExperimentalStackAnimator =
    experimentalStackAnimator(
        animationSpec = animationSpec
    ) { factor, direction ->
        when (direction) {
            Direction.ENTER_FRONT,
            Direction.EXIT_FRONT,
                -> Modifier.offsetXFactor { factor }
            Direction.ENTER_BACK,
            Direction.EXIT_BACK,
                -> Modifier.offsetXFactor { factor * SlideInFastOutSlowBackFactor }
        }
    }

@OptIn(ExperimentalDecomposeApi::class)
fun slidePredictiveBackAnimatable(
    initialBackEvent: BackEvent,
    animationSpec: FiniteAnimationSpec<Float> = navigationAnimationSpec(),
): PredictiveBackAnimatable = SlidePredictiveBackAnimatable(
    initialBackEvent = initialBackEvent,
    getExitModifier = { progress, _ -> Modifier.offsetXFactor { progress } },
    getEnterModifier = { progress, _ -> Modifier.offsetXFactor { (progress - 1) * SlideInFastOutSlowBackFactor } },
    maxSwipeProgressFactor = PredictiveBackMaxSwipeProgressFactor,
    animationSpec = animationSpec,
)

@OptIn(ExperimentalDecomposeApi::class)
private class SlidePredictiveBackAnimatable(
    initialBackEvent: BackEvent,
    private val getExitModifier: (progress: Float, edge: BackEvent.SwipeEdge) -> Modifier,
    private val getEnterModifier: (progress: Float, edge: BackEvent.SwipeEdge) -> Modifier,
    private val maxSwipeProgressFactor: Float,
    private val animationSpec: FiniteAnimationSpec<Float>,
) : PredictiveBackAnimatable {

    private val progressAnimatable = Animatable(initialValue = initialBackEvent.progress)

    private var swipeEdge by mutableStateOf(initialBackEvent.swipeEdge)

    override val exitModifier: Modifier
        get() = getExitModifier(progressAnimatable.value, swipeEdge)
    override val enterModifier: Modifier
        get() = getEnterModifier(progressAnimatable.value, swipeEdge)

    override suspend fun animate(event: BackEvent) {
        swipeEdge = event.swipeEdge
        progressAnimatable.snapTo(targetValue = event.progress * maxSwipeProgressFactor)
    }

    override suspend fun finish() {
        progressAnimatable.animateTo(targetValue = 1F, animationSpec = animationSpec)
    }

    override suspend fun cancel() {
        progressAnimatable.animateTo(targetValue = 0F, animationSpec = animationSpec)
    }
}

private fun Modifier.offsetXFactor(factor: () -> Float): Modifier =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        layout(placeable.width, placeable.height) {
            placeable.placeRelative(x = (placeable.width.toFloat() * factor()).toInt(), y = 0)
        }
    }


private fun <T> navigationAnimationSpec(): FiniteAnimationSpec<T> {
    return tween(NavigationAnimationDuration, easing = FastOutSlowInEasing)
}
