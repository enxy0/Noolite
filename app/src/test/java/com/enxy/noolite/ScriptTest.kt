package com.enxy.noolite

import com.enxy.noolite.core.data.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ScriptTest {
    lateinit var groupList: ArrayList<Group>

    @Before
    fun `set up`() {
        groupList = arrayListOf(
            Group(
                0, "Kitchen", arrayListOf(
                    Channel(0, "Main", 2),
                    Channel(1, "Backlight", 3)
                )
            ),
            Group(
                1, "Living Room", arrayListOf(
                    Channel(2, "Main", 1),
                    Channel(3, "Backlight", 2)
                )
            ),
            Group(
                2, "Bathroom", arrayListOf(
                    Channel(4, "Main", 0),
                    Channel(5, "Above the toilet", 3)
                )
            )
        )

    }

    @Test
    fun `script writing and removing data`() {
        // Writing data
        val script = Script("Test", ArrayList()).apply {
            write(groupList[0], Action.TURN_OFF)
            write(groupList[1], Action.TURN_ON)
            write(groupList[2], Action.TOGGLE_STATE)
            write(groupList[0].channelList[0], Action.CHANGE_BRIGHTNESS, 50)
        }
        val expectedActionList: ArrayList<ChannelAction> = arrayListOf(
            ChannelAction(0, Action.TURN_OFF, null),
            ChannelAction(1, Action.TURN_OFF, null),
            ChannelAction(2, Action.TURN_ON, null),
            ChannelAction(3, Action.TURN_ON, null),
            ChannelAction(
                4,
                Action.TOGGLE_STATE,
                null
            ),
            ChannelAction(
                5,
                Action.TOGGLE_STATE,
                null
            ),
            ChannelAction(
                0,
                Action.CHANGE_BRIGHTNESS,
                50
            )
        )
        val expectedScript = script.copy(actionsList = expectedActionList)
        assertEquals(expectedScript, script)

        // Removing data
        script.apply {
            remove(groupList[0], Action.TURN_OFF)
            remove(groupList[2], Action.TURN_ON) // should do nothing
            remove(groupList[1], Action.TURN_ON) // should remove group
            for (channel in groupList[2].channelList)
                remove(channel, Action.TOGGLE_STATE)
            remove(groupList[0].channelList[0], Action.CHANGE_BRIGHTNESS)
        }
        expectedScript.actionsList.clear()
        assertEquals(expectedScript, script)
    }
}
