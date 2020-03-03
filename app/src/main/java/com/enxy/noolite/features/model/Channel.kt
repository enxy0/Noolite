package com.enxy.noolite.features.model

import java.io.Serializable


data class Channel(
    val id: Int = 0,
    val name: String = "empty",
    val type: Int = 0
) : Serializable