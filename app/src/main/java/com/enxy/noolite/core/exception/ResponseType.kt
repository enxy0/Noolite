package com.enxy.noolite.core.exception

/**
 * Base Class for handling errors/failures/exceptions.
 */
sealed class Failure {
    object WifiConnectionError : Failure()
    object ServerError : Failure()
    object ResponseBodyIsNull : Failure() // TODO: Remove and replace in code with ServerError
    object DataNotFound : Failure() // Returns when there is no data stored
    object DeserializeError: Failure()

}

sealed class Success {
    object GoodRequest: Success()
}
