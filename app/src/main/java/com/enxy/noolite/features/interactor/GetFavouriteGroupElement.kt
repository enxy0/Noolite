package com.enxy.noolite.features.interactor

import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.functional.Either
import com.enxy.noolite.core.functional.Either.Left
import com.enxy.noolite.core.functional.Either.Right
import com.enxy.noolite.core.interactor.UseCase
import com.enxy.noolite.core.interactor.UseCase.None
import com.enxy.noolite.core.platform.FileManager
import com.enxy.noolite.core.platform.Serializer
import com.enxy.noolite.features.model.GroupModel

class GetFavouriteGroupElement(
    private val serializer: Serializer,
    private val fileManager: FileManager
) : UseCase<GroupModel, None>() {

    override suspend fun run(params: None): Either<Failure, GroupModel> {
        val groupModelString = fileManager.getStringFromPrefs(FileManager.MAIN_DATA_FILE, FileManager.FAVOURITE_GROUP_KEY)
        return if (groupModelString != null) {
            val groupElement = serializer.deserialize(groupModelString, GroupModel::class.java)
            Right(groupElement)
        } else {
            Left(Failure.DataNotFound)
        }
    }
}