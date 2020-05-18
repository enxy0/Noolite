package com.enxy.noolite.core.data

class Dummy {
    companion object {
        val favouriteGroup = Group(
            id = 1,
            name = "Кухня",
            channelList = arrayListOf(
                Channel(0, "Над столом", 0),
                Channel(0, "Подсветка стола", 3)
            )
        )

        val groupList = arrayListOf(
            Group(
                id = 0,
                name = "Гостинная",
                channelList = arrayListOf(
                    Channel(0, "Основной", 0),
                    Channel(0, "Над столом", 1),
                    Channel(0, "Подсветка", 3)
                )
            ),
            Group(
                id = 1,
                name = "Кухня",
                channelList = arrayListOf(
                    Channel(0, "Над столом", 0),
                    Channel(0, "Подсветка стола", 3)
                )
            ),
            Group(
                id = 2,
                name = "Коридор",
                channelList = arrayListOf(
                    Channel(0, "Основной", 0),
                    Channel(0, "Подсветка", 3)
                )
            ),
            Group(
                id = 0,
                name = "Ванная",
                channelList = arrayListOf(
                    Channel(0, "Ванная", 1),
                    Channel(0, "Туалет", 1)
                )
            )
        )

        val scriptList = arrayListOf(
            Script(name = "Выкл кухня", actionsList = arrayListOf()),
            Script(name = "Выкл весь свет", actionsList = arrayListOf())
        )
    }
}