package com.enxy.noolite.presentation.utils.extensions

import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun pluralResource(
    @PluralsRes resId: Int,
    quantity: Int,
    vararg formatArgs: Any? = emptyArray()
): String = LocalContext.current.resources.getQuantityString(resId, quantity, *formatArgs)
