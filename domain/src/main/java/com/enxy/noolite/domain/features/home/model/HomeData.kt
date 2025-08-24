package com.enxy.noolite.domain.features.home.model

import com.enxy.noolite.domain.features.common.Group
import com.enxy.noolite.domain.features.common.Script

data class HomeData(
    val groups: List<Group>,
    val scripts: List<Script>,
    val favoriteGroup: Group?
) {
    val isEmpty: Boolean
        get() = groups.isEmpty()
}
