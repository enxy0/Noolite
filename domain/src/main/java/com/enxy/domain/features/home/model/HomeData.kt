package com.enxy.domain.features.home.model

import com.enxy.domain.features.common.Group
import com.enxy.domain.features.common.Script

data class HomeData(
    val groups: List<Group>,
    val scripts: List<Script>,
    val favoriteGroup: Group?
) {
    val isEmpty: Boolean
        get() = groups.isEmpty()
}
