package com.enxy.noolite.core.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> getActivityViewModel(fragment: Fragment) =
    ViewModelProvider(fragment.activity!!)[T::class.java]
