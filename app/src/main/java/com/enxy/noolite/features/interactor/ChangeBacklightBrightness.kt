package com.enxy.noolite.features.interactor

import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.exception.Success
import com.enxy.noolite.core.functional.Either
import com.enxy.noolite.core.interactor.UseCase
import com.enxy.noolite.core.network.NetworkRepository

class ChangeBacklightBrightness(private val networkRepository: NetworkRepository.NetworkManager) :
    UseCase<Success.GoodRequest, Array<Int>>() {
    override suspend fun run(params: Array<Int>): Either<Failure, Success.GoodRequest> =
        networkRepository.changeBacklightBrightness(params[0], params[1])
}