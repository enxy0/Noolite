package com.enxy.noolite.core.utils.extension

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }

fun String.fromUnderscoreToNormal() = replace('_', ' ')