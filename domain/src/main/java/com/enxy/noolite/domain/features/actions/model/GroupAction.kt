package com.enxy.noolite.domain.features.actions.model

import com.enxy.noolite.domain.features.common.Group
import kotlinx.serialization.Serializable

@Serializable
sealed interface GroupAction {

    val group: Group

    @Serializable
    data class TurnOff(override val group: Group) : GroupAction

    @Serializable
    data class TurnOn(override val group: Group) : GroupAction
}
