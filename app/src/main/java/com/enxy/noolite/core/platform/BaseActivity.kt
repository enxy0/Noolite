package com.enxy.noolite.core.platform

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.enxy.noolite.R
import com.enxy.noolite.core.extension.withColor
import com.enxy.noolite.core.extension.withColorPrimary
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_base.*

abstract class BaseActivity : AppCompatActivity() {

    internal fun notify(@StringRes message: Int) {
        Snackbar.make(fragmentHolder, message, Snackbar.LENGTH_SHORT)
            .withColorPrimary()
            .show()
    }

    internal fun setToolbarTitle(@StringRes message: Int) {
        toolbar.title = title
    }

    internal fun setToolbarTitle(message: String) {
        toolbar.title = title
    }

    internal fun notifyError(@StringRes message: Int) {
        Snackbar.make(fragmentHolder, message, Snackbar.LENGTH_LONG)
            .withColor(ContextCompat.getColor(this, R.color.error))
            .show()
    }
}