package com.enxy.noolite.core.platform

import com.google.gson.Gson
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Serializer @Inject constructor(private val gson: Gson) {

    fun serialize(obj: Any): String = gson.toJson(obj, obj::class.java)

    fun <T> deserialize(obj: String, `class`: Class<T>): T = gson.fromJson(obj, `class`)

    fun <T> deserialize(obj: String, type: Type): T = gson.fromJson(obj, type)
}
