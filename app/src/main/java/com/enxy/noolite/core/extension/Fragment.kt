package com.enxy.noolite.core.extension

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.enxy.noolite.core.platform.BaseFragment
import com.enxy.noolite.features.MainActivity
import kotlinx.android.synthetic.main.activity_base.*

inline fun <reified T : ViewModel> getActivityViewModel(fragment: Fragment) =
    ViewModelProviders.of(fragment.activity!!)[T::class.java]

val BaseFragment.viewContainer: View get() = (activity as MainActivity).fragmentHolder
