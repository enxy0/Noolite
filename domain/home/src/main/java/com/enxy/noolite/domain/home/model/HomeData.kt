package com.enxy.noolite.domain.home.model

import com.enxy.noolite.core.model.Group
import com.enxy.noolite.core.model.Script

data class HomeData(
    val groups: List<Group>,
    val scripts: List<Script>,
    val favoriteGroup: Group?
) {
    val isEmpty: Boolean
        get() = groups.isEmpty()
}