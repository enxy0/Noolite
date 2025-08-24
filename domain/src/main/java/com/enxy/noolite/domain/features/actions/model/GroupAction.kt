package com.enxy.noolite.domain.features.actions.model

import com.enxy.noolite.domain.features.common.Group

sealed class GroupAction(open val group: Group) {
    data class TurnOff(override val group: Group) : GroupAction(group)
    data class TurnOn(override val group: Group) : GroupAction(group)
}
