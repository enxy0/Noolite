package com.enxy.noolite.utils.extensions

inline fun <reified T> Iterable<*>.firstOfTypeOrNull() = firstNotNullOfOrNull { it as? T }
