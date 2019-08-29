package com.enxy.noolite.core.extension

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }

fun String.fromUnderscoreToNormal() = replace('_', ' ')