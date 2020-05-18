package com.enxy.noolite.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.enxy.noolite.features.MainActivity
import kotlinx.android.synthetic.main.toolbar.*


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

    override fun onResume() {
        super.onResume()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        getMainActivity().setSupportActionBar(toolbar)
    }

    internal fun setToolbarTitle(@StringRes message: Int) {
        toolbar.setTitle(message)
    }

    internal fun setToolbarTitle(message: String) {
        toolbar.title = message
    }

    internal fun close() {
        parentFragmentManager.popBackStack()
    }

    internal fun showKeyboardOnView(viewToFocus: View) {
        viewToFocus.requestFocus()
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    internal fun getMainActivity(): MainActivity = requireActivity() as MainActivity
}