package com.enxy.noolite.core.utils.extension

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

fun View.toggleVisibility() {
    if (this.isVisible)
        this.isInvisible = true
    else
        this.isVisible = true
}

inline val View.screenWidth: Int
    get() = context!!.screenWidth