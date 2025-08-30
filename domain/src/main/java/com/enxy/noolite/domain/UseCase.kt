package com.enxy.noolite.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

interface UseCase<in P, R> {
    operator fun invoke(param: P): Flow<Result<R>>
}

inline fun <reified T> Flow<T>.asResult(): Flow<Result<T>> = map { Result.success(it) }
    .catch { emit(Result.failure(it)) }
