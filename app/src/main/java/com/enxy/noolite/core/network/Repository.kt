package com.enxy.noolite.core.network

import BinParser
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.exception.Success
import com.enxy.noolite.core.functional.Either
import com.enxy.noolite.core.functional.Either.Left
import com.enxy.noolite.core.functional.Either.Right
import com.enxy.noolite.core.platform.FileManager
import com.enxy.noolite.core.platform.Serializer
import com.enxy.noolite.features.model.GroupListHolderModel
import com.enxy.noolite.features.model.GroupModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val connectionManager: ConnectionManager,
    private val service: NetworkService,
    private val fileManager: FileManager,
    private val serializer: Serializer
) {
    companion object {
        // URLs
        var API_URL = "http://${FileManager.DEFAULT_IP_ADDRESS_VALUE}"
            set(value) {
                field = "http://$value"
            }
        const val API_PAGE = "/api.htm"
        const val SERVER_SETTINGS_FILE = "/noolite_settings.bin"

        // Commands
        const val TURN_OFF_COMMAND = 0
        const val TURN_ON_COMMAND = 2
        const val TOGGLE_COMMAND = 4
        const val FM = 3
        const val CHANGE_BACKLIGHT_COMMAND = 6
        const val CHANGE_COLOR_COMMAND = 17
        const val START_OVERFLOW_COMMAND = 16
        const val STOP_OVERFLOW_COMMAND = 10
    }

    suspend fun getFavouriteGroupElement(): Either<Failure, GroupModel> {
        val groupModelString = withContext(Dispatchers.Default) {
            fileManager.getStringFromPrefs(
                FileManager.MAIN_DATA_FILE,
                FileManager.FAVOURITE_GROUP_KEY
            )
        }
        return if (groupModelString != null) {
            val groupElement = withContext(Dispatchers.Default) {
                serializer.deserialize(groupModelString, GroupModel::class.java)
            }
            Right(groupElement)
        } else {
            Left(Failure.DataNotFound)
        }
    }

    suspend fun saveFavouriteGroupElement(groupModel: GroupModel) =
        withContext(Dispatchers.Default) {
            val serializedGroupModel = serializer.serialize(groupModel)
            fileManager.saveStringToPrefs(
                FileManager.MAIN_DATA_FILE, FileManager.FAVOURITE_GROUP_KEY, serializedGroupModel
            )
        }

    suspend fun getGroupHolder(
        ipAddress: String,
        isForceUpdating: Boolean
    ): Either<Failure, GroupListHolderModel> {
        val groupElementList = fileManager.getStringFromPrefs(
            FileManager.MAIN_DATA_FILE,
            FileManager.GROUP_ELEMENT_LIST_KEY
        )
        if (groupElementList != null && !isForceUpdating) {
            val groupHolder =
                serializer.deserialize(groupElementList, GroupListHolderModel::class.java)
            return Right(groupHolder)
        } else if (connectionManager.isWifiConnected()) {
            API_URL = ipAddress
            return request(
                call = {
                    service.getNooliteApi().getGroupsAsync(url = API_URL + SERVER_SETTINGS_FILE)
                },
                transform = ::transformBinToGroupHolder
            )
        } else
            return Left(Failure.WifiConnectionError)
    }

    suspend fun saveGroupGroupHolder(groupListHolderModel: GroupListHolderModel) =
        withContext(Dispatchers.Default) {
            val serializedGroupListHolderModel = serializer.serialize(groupListHolderModel)
            fileManager.saveStringToPrefs(
                FileManager.MAIN_DATA_FILE,
                FileManager.GROUP_ELEMENT_LIST_KEY,
                serializedGroupListHolderModel
            )
        }

    private suspend fun transformBinToGroupHolder(responseBody: ResponseBody): GroupListHolderModel =
        withContext(Dispatchers.Default) {
            val inputStream = responseBody.byteStream()
            val groupElementList = BinParser.parseData(inputStream.readBytes())
            GroupListHolderModel(groupElementList)
        }

    private suspend fun transformToSuccessRequest(responseBody: ResponseBody): Success.GoodRequest {
        return Success.GoodRequest
    }

    suspend fun changeLightState(channelId: Int): Either<Failure, Success.GoodRequest> {
        return if (connectionManager.isWifiConnected())
            request(
                call = {
                    service.getNooliteApi().changeLightsStateAsync(
                        url = API_URL + API_PAGE,
                        channelAddress = channelId,
                        command = TOGGLE_COMMAND
                    )
                },
                transform = ::transformToSuccessRequest
            )
        else
            Left(Failure.WifiConnectionError)
    }

    suspend fun changeBacklightColor(channelId: Int): Either<Failure, Success.GoodRequest> {
        return if (connectionManager.isWifiConnected())
            request(
                call = {
                    service.getNooliteApi().changeLightsStateAsync(
                        url = API_URL + API_PAGE,
                        channelAddress = channelId,
                        command = CHANGE_COLOR_COMMAND
                    )
                },
                transform = ::transformToSuccessRequest
            )
        else
            Left(Failure.WifiConnectionError)
    }

    suspend fun turnOnLight(channelId: Int): Either<Failure, Success.GoodRequest> {
        return if (connectionManager.isWifiConnected())
            request(
                call = {
                    service.getNooliteApi().changeLightsStateAsync(
                        url = API_URL + API_PAGE,
                        channelAddress = channelId,
                        command = TURN_ON_COMMAND
                    )
                },
                transform = ::transformToSuccessRequest
            )
        else
            Left(Failure.WifiConnectionError)
    }

    suspend fun turnOffLight(channelId: Int): Either<Failure, Success.GoodRequest> {
        return if (connectionManager.isWifiConnected())
            request(
                call = {
                    service.getNooliteApi().changeLightsStateAsync(
                        url = API_URL + API_PAGE,
                        channelAddress = channelId,
                        command = TURN_OFF_COMMAND
                    )
                },
                transform = ::transformToSuccessRequest
            )
        else
            Left(Failure.WifiConnectionError)
    }

    suspend fun changeBacklightBrightness(
        channelId: Int,
        brightness: Int
    ): Either<Failure, Success.GoodRequest> {
        return if (connectionManager.isWifiConnected())
            request(
                call = {
                    service.getNooliteApi().changeBacklightStateAsync(
                        url = API_URL + API_PAGE,
                        channelAddress = channelId,
                        command = CHANGE_BACKLIGHT_COMMAND,
                        fm = FM,
                        brightness = brightness
                    )
                },
                transform = ::transformToSuccessRequest
            )
        else
            Left(Failure.WifiConnectionError)
    }

    suspend fun startBacklightOverflow(channelId: Int): Either<Failure, Success.GoodRequest> {
        return if (connectionManager.isWifiConnected())
            request(
                call = {
                    service.getNooliteApi().changeLightsStateAsync(
                        url = API_URL + API_PAGE,
                        channelAddress = channelId,
                        command = START_OVERFLOW_COMMAND
                    )
                },
                transform = ::transformToSuccessRequest
            )
        else
            Left(Failure.WifiConnectionError)
    }

    suspend fun stopBacklightOverflow(channelId: Int): Either<Failure, Success.GoodRequest> {
        return if (connectionManager.isWifiConnected())
            request(
                call = {
                    service.getNooliteApi().changeLightsStateAsync(
                        url = API_URL + API_PAGE,
                        channelAddress = channelId,
                        command = STOP_OVERFLOW_COMMAND
                    )
                },
                transform = ::transformToSuccessRequest
            )
        else
            Left(Failure.WifiConnectionError)
    }

    private suspend fun <T> request(
        call: suspend () -> Response<ResponseBody>,
        transform: suspend (ResponseBody) -> T
    ): Either<Failure, T> {
        return try {
            val response: Response<ResponseBody> = withContext(Dispatchers.IO) { call.invoke() }
            when (response.isSuccessful) {
                true -> {
                    val body = response.body()
                    if (body != null)
                        Right(transform(body))
                    else
                        Left(Failure.ResponseBodyIsNull)
                }
                false -> Left(Failure.ServerError)
            }
        } catch (e: Throwable) {
            Left(Failure.ServerError)
        }
    }
}