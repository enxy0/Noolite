package com.enxy.noolite.core.ui.extensions

inline fun <reified T> Iterable<*>.firstOfTypeOrNull() = firstNotNullOfOrNull { it as? T }
