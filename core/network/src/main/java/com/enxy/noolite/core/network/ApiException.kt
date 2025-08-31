package com.enxy.noolite.core.network

import java.io.IOException

internal open class ApiException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : IOException()
