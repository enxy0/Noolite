package com.enxy.noolite.core.extension

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

private const val snackBarCornerRadiusInDp = 2f
private const val snackBarDefaultMarginInDp = 8f
private const val snackBarElevationInPx = 6f

fun Snackbar.withColor(@ColorInt color: Int): Snackbar {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    val gradientDrawable = GradientDrawable()
    val snackbarTextView = this.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    val margin = context.convertDpToPixel(snackBarDefaultMarginInDp).toInt()
    val cornerRadius = context.convertDpToPixel(snackBarCornerRadiusInDp)
    params.setMargins(margin, 0, margin, margin)
    gradientDrawable.cornerRadius = cornerRadius
    gradientDrawable.setColor(color)
    snackbarTextView.setTextColor(ContextCompat.getColor(this.view.context, android.R.color.white))
    ViewCompat.setElevation(this.view, snackBarElevationInPx)

    this.view.layoutParams = params
    this.view.background = gradientDrawable
    return this
}


fun Snackbar.withColorPrimary(): Snackbar {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    val gradientDrawable = GradientDrawable()
    val typedValue = TypedValue()
    val margin = context.convertDpToPixel(snackBarDefaultMarginInDp).toInt()
    val snackbarTextView = this.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    val cornerRadius = context.convertDpToPixel(snackBarCornerRadiusInDp)
    val color = if (context.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true))
        typedValue.data
    else
        Color.DKGRAY

    params.setMargins(margin, 0, margin, margin)
    gradientDrawable.setColor(color)
    gradientDrawable.cornerRadius = cornerRadius
    snackbarTextView.setTextColor(ContextCompat.getColor(this.view.context, android.R.color.white))
    ViewCompat.setElevation(this.view, snackBarElevationInPx)

    this.view.layoutParams = params
    this.view.background = gradientDrawable
    return this
}