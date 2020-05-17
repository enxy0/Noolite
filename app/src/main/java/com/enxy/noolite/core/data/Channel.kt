package com.enxy.noolite.core.data

import java.io.Serializable


data class Channel(
    val id: Int = 0,
    val name: String = "empty",
    val type: Int = 0
) : Serializable