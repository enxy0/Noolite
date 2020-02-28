package com.enxy.noolite.core.extension

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

fun View.toggleVisibility() {
    if (this.isVisible)
        this.isInvisible = true
    else
        this.isVisible = true
}