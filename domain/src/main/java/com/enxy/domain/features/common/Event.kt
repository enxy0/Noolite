package com.enxy.domain.features.common

class Event<out T>(private val value: T) {
    private var isHandled: Boolean = false

    fun getValueIfNotHandled(): T? = value.takeIf { !isHandled }?.also { isHandled = true }
    fun getValue(): T = value
}
