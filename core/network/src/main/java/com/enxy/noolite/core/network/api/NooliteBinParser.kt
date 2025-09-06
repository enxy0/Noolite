package com.enxy.noolite.core.network.api

import com.enxy.noolite.core.model.Channel
import com.enxy.noolite.core.model.Group
import java.io.ByteArrayOutputStream

/**
 * [NooliteBinParser] parses settings bin file from PR-1132 (/noolite_settings.bin) which contains
 * information about groups and channels and how they connected with each other.
 * This class was decompiled from original Noolite app (sensors was removed type=2).
 */
class NooliteBinParser {
    private companion object {
        const val NEXT_OFFSET = 24
        const val NEXT_GROUP_OFFSET = 32
        const val NEXT_CHANNEL_OFFSET = 25

        const val MAX_GROUPS = 16
        const val MAX_CHANNELS = 32
        const val CHANNELS_IN_GROUP = 8

        const val START_FILE_OFFSET = 6 // identifies ethernet-gateway model number (PR1132)
        const val START_GROUP_OFFSET = START_FILE_OFFSET + NEXT_OFFSET
        const val START_CHANNEL_OFFSET = START_FILE_OFFSET + NEXT_GROUP_OFFSET * MAX_GROUPS
        const val START_CHANNEL_TYPE_OFFSET = START_CHANNEL_OFFSET + NEXT_OFFSET
    }

    fun parse(data: ByteArray): List<Group> {
        var groupOffset = 0
        val groups = mutableListOf<Group>()
        for (groupId in 1..MAX_GROUPS) {
            val groupName = getName(data, START_FILE_OFFSET + groupOffset)
            val isGroupVisible = isGroupVisible(data[START_GROUP_OFFSET + groupOffset])
            if (!isGroupVisible) continue
            val channels = mutableListOf<Channel>()
            for (channelPosition in 0 until CHANNELS_IN_GROUP) {
                getChannelId(data[START_GROUP_OFFSET + groupOffset + channelPosition])
                    .minus(1)
                    .takeIf { it >= 0 }
                    ?.let { channelId ->
                        val offset = NEXT_CHANNEL_OFFSET * channelId
                        channels + Channel(
                            id = channelId,
                            name = getName(data, START_CHANNEL_OFFSET + offset),
                            type = data[START_CHANNEL_TYPE_OFFSET + offset].toInt()
                        )
                    }
            }
            groupOffset += NEXT_GROUP_OFFSET
            groups + Group(groupId, groupName, channels)
        }
        return groups
    }

    private fun getName(data: ByteArray, position: Int): String {
        val name = try {
            ByteArrayOutputStream().use { outputStream ->
                outputStream.write(data, position, NEXT_OFFSET)
                outputStream.toString("cp1251")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
        return name.trim { it <= ' ' }
    }

    private fun getChannelId(b: Byte): Int = b.toInt() and 63

    private fun isGroupVisible(b: Byte): Boolean = b.toInt() shr START_FILE_OFFSET == 0
}
