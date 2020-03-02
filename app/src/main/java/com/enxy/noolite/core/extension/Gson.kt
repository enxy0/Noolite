package com.enxy.noolite.core.extension

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

fun <T> getArrayListBuildType(): Type {
    return object : TypeToken<ArrayList<T>>() {}.type
}