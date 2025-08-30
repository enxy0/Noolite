package com.enxy.noolite.domain.features.demo

import com.enxy.noolite.domain.common.Group

interface StaticInfoDataSource {
    fun getGroups(): List<Group>
}
