package com.enxy.noolite.core.exception

/**
 * Base Class for handling errors/failures/exceptions.
 */
sealed class Failure {
    object WifiConnectionError : Failure()
    object ServerError : Failure()
    object DataNotFound : Failure()
    object DeserializeError: Failure()

}

sealed class Success {
    object GoodRequest: Success()
}
