package com.enxy.noolite.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn


interface UseCase<in P, R> {
    operator fun invoke(param: P): Flow<Result<R>> = execute(param).handleOn()
    fun execute(param: P): Flow<Result<R>>

    fun <T> Flow<Result<T>>.handleOn(
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<Result<T>> = this
        .catch { e -> emit(Result.failure(e)) }
        .flowOn(dispatcher)
}
