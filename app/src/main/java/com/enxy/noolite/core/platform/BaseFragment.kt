package com.enxy.noolite.core.platform

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.enxy.noolite.features.MainActivity
import kotlinx.android.synthetic.main.activity_base.*

abstract class BaseFragment : Fragment() {
    internal fun notify(@StringRes message: Int) {
        getMainActivity().notify(message)
    }

    internal fun notify(message: String) {
        getMainActivity().notify(message)
    }

    internal fun notifyError(@StringRes message: Int) {
        getMainActivity().notifyError(message)
    }

    abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
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