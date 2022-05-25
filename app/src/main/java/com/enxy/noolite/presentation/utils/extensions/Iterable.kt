package com.enxy.noolite.presentation.utils.extensions

inline fun <reified T> Iterable<*>.firstOfTypeOrNull() = firstNotNullOfOrNull { it as? T }
inline fun <reified T> Iterable<*>.firstOfType() = firstNotNullOf { it as? T }
