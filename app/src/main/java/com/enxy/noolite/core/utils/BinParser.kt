package com.enxy.noolite.core.utils

import com.enxy.noolite.core.data.Channel
import com.enxy.noolite.core.data.Group
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.ParseException

/**
 * [BinParser] parses settings bin file from PR-1132 (/noolite_settings.bin) which contains
 * information about groups and channels and how they connected with each other.
 * This class was decompiled from original Noolite app (sensors was removed type=2).
 */
class BinParser {
    companion object {
        private const val START_OFFSET = 6
        private const val OFFSET = 24
        private const val GROUP_OFFSET = 32
        private const val CHANNEL_OFFSET = 25
        private const val MAX_GROUPS = 16
        private const val MAX_CHANNELS = 32
        private const val CHANNELS_IN_GROUP = 8
        private const val FIRST_SIX_BYTES = 63

        fun parseData(data: ByteArray): ArrayList<Group> {
            try {
                val groupArrayList = ArrayList<Group>()
                val currentChannelPosition = GROUP_OFFSET * MAX_GROUPS
                var currentGroupPos = 0
                for (groupId in 1..MAX_GROUPS) {
                    val groupName = getName(data, START_OFFSET + currentGroupPos)
                    val groupVisibility =
                        getVisibilityOfGroup(data[START_OFFSET + OFFSET + currentGroupPos])
                    if (groupVisibility) {
//                    val groupElement = GroupModel(groupId, groupName, true)
                        val groupElement = Group(groupId, groupName)
                        for (currentChannelPos in 0 until CHANNELS_IN_GROUP) {
                            val channelId =
                                getChannelId(
                                    data[START_OFFSET + OFFSET + currentGroupPos + currentChannelPos]
                                )
                            if (channelId != 0) {
                                val channel = Channel(
                                    id = channelId - 1,
                                    name = getName(
                                        data,
                                        START_OFFSET + currentChannelPosition + (channelId - 1) * CHANNEL_OFFSET
                                    ),
                                    type = data[START_OFFSET + currentChannelPosition + (channelId - 1) * CHANNEL_OFFSET + OFFSET].toInt()
                                )
                                groupElement.channelList.add(channel)
                            }
                        }
                        groupArrayList.add(groupElement)
                    }
                    currentGroupPos += GROUP_OFFSET
                }
                return groupArrayList
            } catch (ex: Exception) {
                throw ParseException("Something bad happened. Couldn't parse given bytes!", 0)
            }
        }

        /**
         * Returns the name of Group or Channel by given position.
         */
        private fun getName(data: ByteArray, currentPosition: Int): String {
            var result: String? = null
            try {
                val os = ByteArrayOutputStream()
                os.write(data, currentPosition, OFFSET)
                result = os.toString("cp1251")
                os.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return result?.trim { it <= ' ' } ?: "no name"
        }

        private fun getChannelId(b: Byte): Int {
            return b.toInt() and FIRST_SIX_BYTES
        }

        private fun getVisibilityOfGroup(b: Byte): Boolean {
            return b.toInt() shr START_OFFSET == 0
        }
    }
}
