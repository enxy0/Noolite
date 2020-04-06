package com.enxy.noolite.core.utils.extension

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

val Context.screenWidth: Int
    get() = Point().also {
        (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(it)
    }.x
