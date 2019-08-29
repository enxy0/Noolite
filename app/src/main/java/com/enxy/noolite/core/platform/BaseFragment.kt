package com.enxy.noolite.core.platform

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.enxy.noolite.R
import com.enxy.noolite.core.extension.viewContainer
import com.enxy.noolite.core.extension.withColor
import com.enxy.noolite.core.extension.withColorPrimary
import com.enxy.noolite.features.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_base.*

abstract class BaseFragment : Fragment() {
    internal fun notify(@StringRes message: Int) {
        Snackbar.make(viewContainer, message, Snackbar.LENGTH_SHORT)
            .withColorPrimary()
            .show()
    }

    abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    internal fun notifyError(@StringRes message: Int) {
        Snackbar.make(viewContainer, message, Snackbar.LENGTH_LONG)
            .withColor(ContextCompat.getColor(context!!, R.color.error))
            .show()
    }

    internal fun isInLandscapeOrientation(): Boolean {
        val orientation = resources.configuration.orientation
        return orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    internal fun setUpBackButton() {
        getMainActivity().supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        getMainActivity().supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    internal fun hideBackButton() {
        getMainActivity().supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        getMainActivity().supportActionBar!!.setDisplayShowHomeEnabled(false)
    }

    internal fun setToolbarTitle(@StringRes message: Int) {
        getMainActivity().toolbar.setTitle(message)
    }

    internal fun setToolbarTitle(message: String) {
        getMainActivity().toolbar.title = message
    }

    internal fun getMainActivity() = activity!! as MainActivity
}