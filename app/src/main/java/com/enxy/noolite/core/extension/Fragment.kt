package com.enxy.noolite.core.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

inline fun <reified T : ViewModel> getActivityViewModel(fragment: Fragment) =
    ViewModelProviders.of(fragment.activity!!)[T::class.java]
