package com.enxy.noolite.features.interactor

import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.functional.Either
import com.enxy.noolite.core.interactor.UseCase
import com.enxy.noolite.core.network.NetworkRepository
import com.enxy.noolite.core.platform.FileManager
import com.enxy.noolite.core.platform.Serializer
import com.enxy.noolite.features.interactor.GetGroupHolder.ParamsHolder
import com.enxy.noolite.features.model.GroupListHolderModel
import kotlinx.coroutines.Job
import javax.inject.Inject

class GetGroupHolder @Inject constructor(
    job: Job,
    private val serializer: Serializer,
    private val networkManager: NetworkRepository.NetworkManager,
    private val fileManager: FileManager
) : UseCase<GroupListHolderModel, ParamsHolder>(job) {

    override suspend fun run(params: ParamsHolder): Either<Failure, GroupListHolderModel> {
        val groupElementList = fileManager.getStringFromPrefs(FileManager.MAIN_DATA_FILE, FileManager.GROUP_ELEMENT_LIST_KEY)
        return if (groupElementList != null && !params.isForceUpdating) {
            val groupHolder = serializer.deserialize(groupElementList, GroupListHolderModel::class.java)
            Either.Right(groupHolder)
        } else {
            return networkManager.getGroupHolder(params.ipAddress)
        }
    }

    data class ParamsHolder(
        val isForceUpdating: Boolean,
        val ipAddress: String
    )

}