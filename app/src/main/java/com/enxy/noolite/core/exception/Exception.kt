package com.enxy.noolite.core.exception

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific groupFailure should extend [FeatureFailure] class.
 */
sealed class Failure {
    object WifiConnectionError : Failure()
    object ServerError : Failure()
    object DataNotFound : Failure()
    object DeserializeError: Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure : Failure()
}

sealed class Success {
    object GoodRequest: Success()
}
