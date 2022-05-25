package com.enxy.data.net

import java.io.IOException

open class ApiException(
    override val message: String? = null,
    override val cause: Throwable? = null,
    val code: Int? = null
) : IOException()
