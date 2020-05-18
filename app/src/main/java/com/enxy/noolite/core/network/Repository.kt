package com.enxy.noolite.core.network

import com.enxy.noolite.core.data.Group
import com.enxy.noolite.core.data.Script
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.exception.Failure.ServerError
import com.enxy.noolite.core.exception.Success
import com.enxy.noolite.core.functional.Either
import com.enxy.noolite.core.functional.Either.Left
import com.enxy.noolite.core.functional.Either.Right
import com.enxy.noolite.core.utils.BinParser
import com.enxy.noolite.core.utils.Constants.Companion.DEFAULT_IP_ADDRESS_VALUE
import com.enxy.noolite.core.utils.Constants.Companion.FAVOURITE_GROUP_KEY
import com.enxy.noolite.core.utils.Constants.Companion.GROUP_LIST_KEY
import com.enxy.noolite.core.utils.Constants.Companion.MAIN_DATA_FILE
import com.enxy.noolite.core.utils.Constants.Companion.SCRIPT_LIST_KEY
import com.enxy.noolite.core.utils.FileManager
import com.enxy.noolite.core.utils.extension.fromJson
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * Repository module for handling data operations.
 */
class Repository(
    private val connectionManager: ConnectionManager,
    private val service: NetworkService,
    private val fileManager: FileManager,
    private val gson: Gson
) {
    // TODO: Rewrite Retrofit network calls with api
    companion object {
        // URLs
        var API_URL = "http://${DEFAULT_IP_ADDRESS_VALUE}"
            set(value) {
                field = "http://$value"
            }
        const val API_PAGE = "/api.htm"
        const val SERVER_SETTINGS_FILE = "/noolite_settings.bin"

        // TODO: Move commands to somewhere else ? 
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

    suspend fun saveScripts(scriptList: ArrayList<Script>) = withContext(Dispatchers.Default) {
        val scriptListJson: String = gson.toJson(scriptList)
        fileManager.saveStringToPrefs(
            MAIN_DATA_FILE, SCRIPT_LIST_KEY, scriptListJson
        )
    }

    suspend fun getScripts(): Either<Failure, ArrayList<Script>> {
        val json: String? = withContext(Dispatchers.IO) {
            fileManager.getStringFromPrefs(MAIN_DATA_FILE, SCRIPT_LIST_KEY)
        }
        return if (json != null) {
            val scriptList: ArrayList<Script> = withContext(Dispatchers.Default) {
                gson.fromJson<ArrayList<Script>>(json)
            }
            Right(scriptList)
        } else
            Left(Failure.DataNotFound)
    }

    suspend fun getFavouriteGroup(): Either<Failure, Group> {
        val groupJson = withContext(Dispatchers.IO) {
            fileManager.getStringFromPrefs(
                MAIN_DATA_FILE, FAVOURITE_GROUP_KEY
            )
        }
        return if (groupJson != null) {
            val group: Group = withContext(Dispatchers.Default) {
                gson.fromJson<Group>(groupJson)
            }
            Right(group)
        } else {
            Left(Failure.DataNotFound)
        }
    }

    suspend fun saveFavouriteGroupElement(group: Group) = withContext(Dispatchers.IO) {
        val groupJson: String = withContext(Dispatchers.Default) { gson.toJson(group) }
        fileManager.saveStringToPrefs(
            MAIN_DATA_FILE, FAVOURITE_GROUP_KEY, groupJson
        )
    }

    suspend fun removeFavouriteGroup() = withContext(Dispatchers.IO) {
        fileManager.removeString(MAIN_DATA_FILE, FAVOURITE_GROUP_KEY)
    }

    suspend fun getGroupList(
        ipAddress: String,
        isForceUpdating: Boolean
    ): Either<Failure, ArrayList<Group>> {
        val groupListJson: String? = withContext(Dispatchers.IO) {
            fileManager.getStringFromPrefs(MAIN_DATA_FILE, GROUP_LIST_KEY)
        }
        return if (groupListJson != null && !isForceUpdating) {
            val groupList: ArrayList<Group> = withContext(Dispatchers.Default) {
                gson.fromJson<ArrayList<Group>>(groupListJson)
            }
            Right(groupList)
        } else if (connectionManager.isWifiConnected()) {
            API_URL = ipAddress
            request(
                call = {
                    service.getNooliteApi().getGroupsAsync(url = API_URL + SERVER_SETTINGS_FILE)
                },
                transform = ::transformBinToGroupList
            )
        } else
            Left(Failure.WifiConnectionError)
    }

    suspend fun saveGroupList(groupList: ArrayList<Group>) =
        withContext(Dispatchers.Default) {
            val groupListJson = gson.toJson(groupList)
            fileManager.saveStringToPrefs(
                MAIN_DATA_FILE, GROUP_LIST_KEY, groupListJson
            )
        }

    private suspend fun transformBinToGroupList(responseBody: ResponseBody): ArrayList<Group> =
        withContext(Dispatchers.Default) {
            val inputStream = responseBody.byteStream()
            val groupList = BinParser.parseData(inputStream.readBytes())
            groupList
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
                        Left(ServerError)
                }
                false -> Left(ServerError)
            }
        } catch (e: Throwable) {
            Left(ServerError)
        }
    }
}