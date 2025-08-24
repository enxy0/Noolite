package com.enxy.noolite.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.enxy.noolite.data.net.noolite.NooliteBinParser
import com.enxy.noolite.domain.features.common.Channel
import com.enxy.noolite.domain.features.common.Group
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NooliteSettingsTest {
    private val group1 = Group(1, "Коридор", listOf(Channel(0, "Коридор", 0), Channel(1, "Прихожая", 0)), false)
    private val group2 = Group(2, "Никита / Тима", listOf(Channel(2, "Zone Тима", 0), Channel(3, "Zone Никита", 0)), false)
    private val group3 = Group(3, "Гостинная", listOf(Channel(4, "Над диваном", 0), Channel(5, "Основной", 0), Channel(6, "Подсветка", 3)), false)
    private val group4 = Group(4, "Спальня", listOf(Channel(7, "Основной", 0), Channel(8, "Подсветка", 3)), false)
    private val group5 = Group(5, "Ванна", listOf(Channel(9, "Над унитазом", 0), Channel(10, "Над ванной", 0)), false)
    private val group6 = Group(6, "Кухня", listOf(Channel(11, "Кухня", 0), Channel(12, "Стол", 1), Channel(13, "Подсветка кухни", 3), Channel(14, "Подсветка стол", 3)), false)
    private val groupsExpected = arrayOf(group1, group2, group3, group4, group5, group6)

    @Test
    fun testSettingsParsing() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val bin = appContext.resources.openRawResource(R.raw.noolite_settings)
        val result = NooliteBinParser.parse(bin.readBytes())
        Assert.assertArrayEquals(groupsExpected, result.toTypedArray())
    }
}
