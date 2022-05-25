package com.enxy.noolite.presentation.utils.extensions

import android.content.Context
import androidx.lifecycle.AndroidViewModel

val AndroidViewModel.context: Context
    get() = getApplication()
