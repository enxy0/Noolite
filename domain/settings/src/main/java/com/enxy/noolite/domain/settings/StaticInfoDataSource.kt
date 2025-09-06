package com.enxy.noolite.domain.settings

import com.enxy.noolite.core.model.Group

interface StaticInfoDataSource {
    fun getGroups(): List<Group>
}
