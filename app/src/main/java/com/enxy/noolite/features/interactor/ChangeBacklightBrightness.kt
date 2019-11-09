package com.enxy.noolite.features.interactor

import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.exception.Success
import com.enxy.noolite.core.functional.Either
import com.enxy.noolite.core.interactor.UseCase
import com.enxy.noolite.core.network.NetworkRepository
import kotlinx.coroutines.Job
import javax.inject.Inject

class ChangeBacklightBrightness @Inject constructor(
    job: Job,
    private val networkRepository: NetworkRepository.NetworkManager
) :
    UseCase<Success.GoodRequest, Array<Int>>(job) {
    override suspend fun run(params: Array<Int>): Either<Failure, Success.GoodRequest> =
        networkRepository.changeBacklightBrightness(params[0], params[1])
}