package com.enxy.noolite.features.model

class TestData {
    companion object {
        val favouriteGroupElement = GroupModel(
            0,
            "Living Room",
            arrayListOf(
                ChannelModel(0, "Main", 0),
                ChannelModel(0, "Table", 1),
                ChannelModel(0, "Backlight", 3)
            )
        )

        val groupElementList = arrayListOf(
            GroupModel(
                0,
                "Living Room",
                arrayListOf(
                    ChannelModel(0, "Main", 0),
                    ChannelModel(0, "Table", 1),
                    ChannelModel(0, "Backlight", 3)
                )
            ),
            GroupModel(
                1,
                "Kitchen",
                arrayListOf(
                    ChannelModel(0, "Table", 0),
                    ChannelModel(0, "Backlight", 3)
                )
            ),
            GroupModel(
                2,
                "Hall",
                arrayListOf(
                    ChannelModel(0, "Main", 0),
                    ChannelModel(0, "Backlight", 3)
                )
            ),
            GroupModel(
                0,
                "Bath Room",
                arrayListOf(
                    ChannelModel(0, "Bath", 1),
                    ChannelModel(0, "Toilet", 1)
                )
            )
        )
    }
}