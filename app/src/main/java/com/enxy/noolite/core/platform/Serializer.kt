package com.enxy.noolite.core.platform

import com.google.gson.Gson

class Serializer {
    private val gson = Gson()

    fun serialize(obj: Any): String = gson.toJson(obj, obj::class.java)

    fun <T> deserialize(obj: String, `class`: Class<T>): T = gson.fromJson(obj, `class`)
}
