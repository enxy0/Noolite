package com.enxy.noolite.core.model

import kotlinx.coroutines.flow.Flow

interface UseCase<in P, R> {
    operator fun invoke(param: P): Flow<Result<R>>
}
