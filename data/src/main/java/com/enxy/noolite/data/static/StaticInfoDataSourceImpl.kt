package com.enxy.noolite.data.static

import com.enxy.noolite.domain.features.common.Channel
import com.enxy.noolite.domain.features.common.Group
import com.enxy.noolite.domain.features.demo.StaticInfoDataSource

internal class StaticInfoDataSourceImpl : StaticInfoDataSource {
    override fun getGroups(): List<Group> = buildList {
        add(getHallGroup())
        add(getKitchenGroup())
        add(getToiletGroup())
        add(getLivingRoomGroup())
    }

    private fun getKitchenGroup() = Group(
        id = 1,
        name = "Кухня",
        channels = listOf(
            Channel(
                id = 2,
                name = "Основной",
                type = 0
            ),
            Channel(
                id = 3,
                name = "Над столом",
                type = 1
            ),
            Channel(
                id = 4,
                name = "Подсветка",
                type = 3
            )
        )
    )

    private fun getHallGroup() = Group(
        id = 5,
        name = "Коридор",
        channels = listOf(
            Channel(
                id = 6,
                name = "Основной",
                type = 0
            ),
            Channel(
                id = 7,
                name = "Основной",
                type = 0
            )
        )
    )

    private fun getToiletGroup() = Group(
        id = 8,
        name = "Уборная",
        channels = listOf(
            Channel(
                id = 9,
                name = "Основной",
                type = 0
            ),
            Channel(
                id = 10,
                name = "Над ванной",
                type = 3
            )
        )
    )

    private fun getLivingRoomGroup() = Group(
        id = 11,
        name = "Гостинная",
        channels = listOf(
            Channel(
                id = 12,
                name = "Основной",
                type = 1
            ),
            Channel(
                id = 13,
                name = "Дополнительный",
                type = 1
            ),
            Channel(
                id = 14,
                name = "Подсветка",
                type = 3
            )
        )
    )
}
