package com.enxy.noolite.features.model

class TestData {
    companion object {
        val favouriteGroupElement = Group(
            0,
            "Living Room",
            arrayListOf(
                Channel(0, "Main", 0),
                Channel(0, "Table", 1),
                Channel(0, "Backlight", 3)
            )
        )

        val groupElementList = arrayListOf(
            Group(
                0,
                "Living Room",
                arrayListOf(
                    Channel(0, "Main", 0),
                    Channel(0, "Table", 1),
                    Channel(0, "Backlight", 3)
                )
            ),
            Group(
                1,
                "Kitchen",
                arrayListOf(
                    Channel(0, "Table", 0),
                    Channel(0, "Backlight", 3)
                )
            ),
            Group(
                2,
                "Hall",
                arrayListOf(
                    Channel(0, "Main", 0),
                    Channel(0, "Backlight", 3)
                )
            ),
            Group(
                0,
                "Bath Room",
                arrayListOf(
                    Channel(0, "Bath", 1),
                    Channel(0, "Toilet", 1)
                )
            )
        )
    }
}