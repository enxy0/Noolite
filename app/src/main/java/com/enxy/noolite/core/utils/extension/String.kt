package com.enxy.noolite.core.utils.extension

/**
 * Capitalizes words all words in given string
 */
fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }

/**
 * Replaces underscores with spaces.
 */
fun String.fromUnderscoreToSpaces() = replace('_', ' ')