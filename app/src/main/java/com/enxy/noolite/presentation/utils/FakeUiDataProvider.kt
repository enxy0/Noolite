package com.enxy.noolite.presentation.utils

import com.enxy.noolite.domain.features.actions.model.ChannelAction
import com.enxy.noolite.domain.features.common.Channel
import com.enxy.noolite.domain.features.common.Group
import com.enxy.noolite.domain.features.common.Script
import com.enxy.noolite.domain.features.home.model.HomeData
import com.enxy.noolite.domain.features.settings.model.AppSettings
import com.enxy.noolite.presentation.ui.script.model.ScriptChannel
import com.enxy.noolite.presentation.ui.script.model.ScriptGroup

object FakeUiDataProvider {
    fun getHomeData() = HomeData(
        getGroups(),
        getScripts(),
        getFavoriteGroup()
    )

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

    fun getScriptGroups() = getGroups().map { group ->
        ScriptGroup(
            group = group,
            expanded = true,
            channels = group.channels.map { channel ->
                ScriptChannel(channel)
            }
        )
    }

    fun getChannelType0() = Channel(name = "Над столом", type = 0)

    fun getChannelType1() = Channel(name = "Подсветка стола", type = 1)

    fun getChannelType3() = Channel(name = "Подсветка всей комнаты", type = 3)
}
