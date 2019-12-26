package com.enxy.noolite.core.platform

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.enxy.noolite.AndroidApplication
import com.enxy.noolite.R
import com.enxy.noolite.core.extension.withColor
import com.enxy.noolite.core.extension.withColorPrimary
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_base.*

abstract class BaseActivity : AppCompatActivity() {

    internal val appComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    fun <T : ViewModel> getViewModel(
        factory: ViewModelProvider.Factory,
        viewModelClass: Class<T>
    ): T {
        return ViewModelProviders.of(this, factory)[viewModelClass]
    }

    internal fun notify(@StringRes message: Int) {
        Snackbar.make(fragmentHolder, message, Snackbar.LENGTH_SHORT)
            .withColorPrimary()
            .show()
    }

    internal fun notify(message: String) {
        Snackbar.make(fragmentHolder, message, Snackbar.LENGTH_SHORT)
            .withColorPrimary()
            .show()
    }

    internal fun notifyError(@StringRes message: Int) {
        Snackbar.make(fragmentHolder, message, Snackbar.LENGTH_SHORT)
            .withColor(ContextCompat.getColor(this, R.color.error))
            .show()
    }

    internal fun setToolbarTitle(@StringRes message: Int) {
        toolbar.title = title
    }

    internal fun setToolbarTitle(message: String) {
        toolbar.title = title
    }
}