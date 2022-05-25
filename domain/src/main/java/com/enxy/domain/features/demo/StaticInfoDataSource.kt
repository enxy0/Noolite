package com.enxy.domain.features.demo

import com.enxy.domain.features.common.Group

interface StaticInfoDataSource {
    fun getGroups(): List<Group>
}
