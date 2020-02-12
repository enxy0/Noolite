package com.enxy.noolite.core.network

import BinParser
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.exception.Success
import com.enxy.noolite.core.functional.Either
import com.enxy.noolite.core.functional.Either.Left
import com.enxy.noolite.core.functional.Either.Right
import com.enxy.noolite.core.platform.FileManager
import com.enxy.noolite.features.model.GroupListHolderModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val connectionManager: ConnectionManager,
    private val service: NetworkService
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

    suspend fun getGroupHolder(ipAddress: String): Either<Failure, GroupListHolderModel> {
        return if (connectionManager.isWifiConnected()) {
            API_URL = ipAddress
            request(
                call = {
                    service.getNooliteApi().getGroupsAsync(url = API_URL + SERVER_SETTINGS_FILE)
                },
                transform = ::transformBinToGroupHolder
            )
        } else
            Left(Failure.WifiConnectionError)
    }

    private fun transformBinToGroupHolder(responseBody: ResponseBody): GroupListHolderModel {
        val inputStream = responseBody.byteStream()
        val groupElementList = BinParser.parseData(inputStream.readBytes())
        return GroupListHolderModel(groupElementList)
    }

    private fun transformToSuccessRequest(responseBody: ResponseBody): Success.GoodRequest {
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
        transform: (ResponseBody) -> T
    ): Either<Failure, T> {
        return try {
            val response: Response<ResponseBody> = withContext(Dispatchers.IO) { call.invoke() }
            when (response.isSuccessful) {
                true -> {
                    val body = response.body()
                    if (body != null)
                        Right(transform(body))
                    else
                        Left(Failure.ServerError)
                }
                false -> Left(Failure.ServerError)
            }
        } catch (e: Throwable) {
            Left(Failure.ServerError)
        }
    }
}