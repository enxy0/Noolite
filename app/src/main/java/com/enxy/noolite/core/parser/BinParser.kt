import com.enxy.noolite.features.model.ChannelModel
import com.enxy.noolite.features.model.GroupModel
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.ParseException


/* This class was decompiled from original Noolite app.
 * I removed sensors and other stuff that I don't have in my house, cause I can't test if its working or not.
 */
object BinParser {
    private const val CHANNEL_OFFSET = 25
    private const val FIRST_SIX_BYTES = 63
    private const val GROUP_OFFSET = 32
    private const val NUMBER_OF_CHANNELS = 32
    private const val NUMBER_OF_CHANNELS_IN_GROUP = 8
    private const val NUMBER_OF_GROUPS = 16
    private const val OFFSET = 24
    private const val START_OFFSET = 6

    fun parseData(data: ByteArray): ArrayList<GroupModel> {
        try {
            val groupArrayList = ArrayList<GroupModel>()
            val currentChannelPosition = GROUP_OFFSET * NUMBER_OF_GROUPS
            var currentGroupPosition = 0
            for (groupId in 1..NUMBER_OF_GROUPS) {
                val groupName = getName(data, START_OFFSET + currentGroupPosition)
                val groupVisibility = getVisibilityOfGroup(data[START_OFFSET + OFFSET + currentGroupPosition])
                if (groupVisibility) {
//                    val groupElement = GroupModel(groupId, groupName, true)
                    val groupElement = GroupModel(groupId, groupName)
                    for (j in 0 until NUMBER_OF_CHANNELS_IN_GROUP) {
                        val channelElementId =
                            getChannelElementId(data[currentGroupPosition + START_OFFSET + OFFSET + j])
                        if (channelElementId != 0) {
                            val channelElement = ChannelModel(
                                channelElementId - 1,
                                getName(
                                    data,
                                    START_OFFSET + currentChannelPosition + (channelElementId - 1) * CHANNEL_OFFSET
                                ),
                                data[START_OFFSET + currentChannelPosition + (channelElementId - 1) * CHANNEL_OFFSET + OFFSET].toInt()
                            )
                            groupElement.channelModelList.add(channelElement)
                        }
                    }
                    groupArrayList.add(groupElement)
                }
                currentGroupPosition += GROUP_OFFSET
            }
            return groupArrayList
        } catch (ex: Exception) {
            throw ParseException("Something bad happened. Couldn't parse given bytes!", 0)
        }
    }


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

    private fun getChannelElementId(b: Byte): Int {
        return b.toInt() and FIRST_SIX_BYTES
    }

    private fun getVisibilityOfGroup(b: Byte): Boolean {
        return b.toInt() shr START_OFFSET == 0
    }
}
