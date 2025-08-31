package com.enxy.noolite.core.ui

import com.enxy.noolite.core.model.AppSettings
import com.enxy.noolite.core.model.Channel
import com.enxy.noolite.core.model.ChannelAction
import com.enxy.noolite.core.model.Group
import com.enxy.noolite.core.model.Script

object FakeUiDataProvider {

    fun getSettings() = AppSettings("192.168.1.10")

    fun getFavoriteGroup() = Group(
        id = 1,
        name = "Кухня",
        channels = listOf(
            Channel(id = 0, name = "Над столом", type = 0),
            Channel(id = 1, name = "Подсветка стола", type = 3),
        )
    )

    fun getScripts(): List<Script> = listOf(
        Script(
            id = 0,
            name = "Выкл кухня",
            actions = listOf(ChannelAction.TurnOff(0))
        ),
        Script(
            id = 1,
            name = "Выкл весь свет",
            actions = listOf(
                ChannelAction.TurnOff(0),
                ChannelAction.TurnOff(1),
                ChannelAction.TurnOff(2),
                ChannelAction.TurnOff(3)
            )
        ),
        Script(
            id = 2,
            name = "Релакс",
            actions = listOf(
                ChannelAction.StartOverflow(2),
                ChannelAction.StartOverflow(3)
            )
        )
    )

    fun getGroups() = listOf(
        Group(
            id = 0,
            name = "Гостинная",
            channels = listOf(
                Channel(0, "Основной", 0),
                Channel(0, "Над столом", 1),
                Channel(0, "Подсветка", 3)
            )
        ),
        Group(
            id = 1,
            name = "Кухня",
            channels = listOf(
                Channel(0, "Над столом", 0),
                Channel(0, "Подсветка стола", 3)
            )
        ),
        Group(
            id = 2,
            name = "Коридор",
            channels = listOf(
                Channel(0, "Основной", 0),
                Channel(0, "Подсветка", 3)
            )
        ),
        Group(
            id = 3,
            name = "Ванная",
            channels = listOf(
                Channel(0, "Ванная", 1),
                Channel(0, "Туалет", 1)
            )
        )
    )

    fun getChannelType0() = Channel(name = "Над столом", type = 0)

    fun getChannelType1() = Channel(name = "Подсветка стола", type = 1)

    fun getChannelType3() = Channel(name = "Подсветка всей комнаты", type = 3)
}