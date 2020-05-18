package com.enxy.noolite.core.utils.extension

import android.content.res.Resources
import android.util.TypedValue

val Int.dp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
    ).toInt()