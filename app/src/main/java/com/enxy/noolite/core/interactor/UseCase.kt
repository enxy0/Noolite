package com.enxy.noolite.core.interactor

import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.functional.Either
import kotlinx.coroutines.*

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means than any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * (Dispatchers.IO) and will post the result in the UI thread.
 */
abstract class UseCase<out Type, in Params>(private val job: Job) where Type : Any {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}) {
        val work = CoroutineScope(Dispatchers.IO + this.job).async { run(params) }
        CoroutineScope(Dispatchers.Main + this.job).launch { onResult(work.await()) }
    }

    class None
}