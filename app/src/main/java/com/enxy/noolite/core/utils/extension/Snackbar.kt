package com.enxy.noolite.core.utils.extension

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.enxy.noolite.R
import com.google.android.material.snackbar.Snackbar

private const val margin = 8
private const val snackBarElevationInPx = 6f

/**
 * [withColor] changes Snackbar background color.
 */
fun Snackbar.withColor(@ColorInt color: Int): Snackbar {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    val gradientDrawable = GradientDrawable()
    val snackbarTextView =
        this.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    params.setMargins(margin.dp, 0, margin.dp, margin.dp)
    gradientDrawable.cornerRadius =
        this.context.resources.getDimension(R.dimen.global_corner_radius)
    gradientDrawable.setColor(color)
    snackbarTextView.setTextColor(ContextCompat.getColor(this.view.context, android.R.color.white))
    ViewCompat.setElevation(this.view, snackBarElevationInPx)

    this.view.layoutParams = params
    this.view.background = gradientDrawable
    return this
}

/**
 * [withColorPrimary] changes Snackbar background color to primaryColor from current theme.
 */
fun Snackbar.withColorPrimary(): Snackbar {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    val gradientDrawable = GradientDrawable()
    val typedValue = TypedValue()
    val snackbarTextView =
        this.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    val color = if (context.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true))
        typedValue.data
    else
        Color.DKGRAY

    params.setMargins(margin.dp, 0, margin.dp, margin.dp)
    gradientDrawable.setColor(color)
    gradientDrawable.cornerRadius =
        this.context.resources.getDimension(R.dimen.global_corner_radius)
    snackbarTextView.setTextColor(ContextCompat.getColor(this.view.context, android.R.color.white))
    ViewCompat.setElevation(this.view, snackBarElevationInPx)

    this.view.layoutParams = params
    this.view.background = gradientDrawable
    return this
}