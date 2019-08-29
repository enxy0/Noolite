package com.enxy.noolite.core.extension

import android.content.Context
import android.util.DisplayMetrics

fun Context.convertDpToPixel(dp: Float): Float {
    return dp * (this.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}
