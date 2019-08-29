package com.enxy.noolite.core.network

import BinParser
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.exception.Success
import com.enxy.noolite.core.functional.Either
import com.enxy.noolite.core.functional.Either.Left
import com.enxy.noolite.core.functional.Either.Right
import com.enxy.noolite.core.platform.FileManager
import com.enxy.noolite.features.model.GroupListHolderModel
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response

interface NetworkRepository {
    suspend fun getGroupHolder(ipAddress: String): Either<Failure, GroupListHolderModel>
    suspend fun turnOnLight(channelId: Int): Either<Failure, Success.GoodRequest>
    suspend fun turnOffLight(channelId: Int): Either<Failure, Success.GoodRequest>
    suspend fun changeBacklightBrightness(channelId: Int, brightness: Int): Either<Failure, Success.GoodRequest>
    suspend fun startBacklightOverflow(channelId: Int): Either<Failure, Success.GoodRequest>
    suspend fun stopBacklightOverflow(channelId: Int): Either<Failure, Success.GoodRequest>
    suspend fun changeLightState(channelId: Int): Either<Failure, Success.GoodRequest>
    suspend fun changeBacklightColor(channelId: Int): Either<Failure, Success.GoodRequest>

    class NetworkManager(private val networkHandler: NetworkHandler) : NetworkRepository {
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

        override suspend fun getGroupHolder(ipAddress: String): Either<Failure, GroupListHolderModel> {
            return if (networkHandler.isWifiConnected()) {
                API_URL = ipAddress
                request(NetworkService.instance.getNooliteApi().getGroupsAsync(API_URL + SERVER_SETTINGS_FILE), ::transformBinToGroupHolder)
            }
            else
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

        override suspend fun changeLightState(channelId: Int): Either<Failure, Success.GoodRequest> {
            return if (networkHandler.isWifiConnected())
                request(NetworkService.instance.getNooliteApi().changeLightsStateAsync(API_URL + API_PAGE, channelId, TOGGLE_COMMAND), ::transformToSuccessRequest)
            else
                Left(Failure.WifiConnectionError)
        }

        override suspend fun changeBacklightColor(channelId: Int): Either<Failure, Success.GoodRequest> {
            return if (networkHandler.isWifiConnected())
                request(NetworkService.instance.getNooliteApi().changeLightsStateAsync(API_URL + API_PAGE, channelId, CHANGE_COLOR_COMMAND), ::transformToSuccessRequest)
            else
                Left(Failure.WifiConnectionError)
        }

        override suspend fun turnOnLight(channelId: Int): Either<Failure, Success.GoodRequest> {
            return if (networkHandler.isWifiConnected())
                request(NetworkService.instance.getNooliteApi().changeLightsStateAsync(API_URL + API_PAGE, channelId, TURN_ON_COMMAND), ::transformToSuccessRequest)
            else
                Left(Failure.WifiConnectionError)
        }

        override suspend fun turnOffLight(channelId: Int): Either<Failure, Success.GoodRequest> {
            return if (networkHandler.isWifiConnected())
                request(NetworkService.instance.getNooliteApi().changeLightsStateAsync(API_URL + API_PAGE, channelId, TURN_OFF_COMMAND), ::transformToSuccessRequest)
            else
                Left(Failure.WifiConnectionError)
        }

        override suspend fun changeBacklightBrightness(channelId: Int, brightness: Int): Either<Failure, Success.GoodRequest> {
            return if (networkHandler.isWifiConnected())
                request(
                    NetworkService.instance.getNooliteApi().changeBacklightStateAsync(API_URL + API_PAGE, channelId, CHANGE_BACKLIGHT_COMMAND, FM, brightness), ::transformToSuccessRequest)
            else
                Left(Failure.WifiConnectionError)
        }

        override suspend fun startBacklightOverflow(channelId: Int): Either<Failure, Success.GoodRequest> {
            return if (networkHandler.isWifiConnected())
                request(NetworkService.instance.getNooliteApi().changeLightsStateAsync(API_URL + API_PAGE, channelId, START_OVERFLOW_COMMAND), ::transformToSuccessRequest)
            else
                Left(Failure.WifiConnectionError)
        }

        override suspend fun stopBacklightOverflow(channelId: Int): Either<Failure, Success.GoodRequest> {
            return if (networkHandler.isWifiConnected())
                request(NetworkService.instance.getNooliteApi().changeLightsStateAsync(API_URL + API_PAGE, channelId, STOP_OVERFLOW_COMMAND), ::transformToSuccessRequest)
            else
                Left(Failure.WifiConnectionError)
        }

        private suspend fun <T> request(call: Deferred<Response<ResponseBody>>, transform: (ResponseBody) -> T): Either<Failure, T> {
            return try {
                val response = call.await()
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
}