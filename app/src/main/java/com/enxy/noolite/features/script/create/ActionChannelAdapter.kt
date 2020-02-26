package com.enxy.noolite.features.script.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enxy.noolite.R
import com.enxy.noolite.features.model.ChannelModel
import kotlinx.android.synthetic.main.item_action_channel.view.*

class ActionChannelAdapter(private val listener: ActionChannelListener) :
    RecyclerView.Adapter<ActionChannelAdapter.ActionChannelHolder>() {
    private val channelList = ArrayList<ChannelModel>()

    fun updateData(groupList: ArrayList<ChannelModel>) {
        this.channelList.clear()
        this.channelList.addAll(groupList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionChannelHolder {
        val view =
            LayoutInflater.from(parent.context!!)
                .inflate(R.layout.item_action_channel, parent, false)
        return ActionChannelHolder(view)
    }

    override fun getItemCount(): Int = channelList.size

    override fun onBindViewHolder(holder: ActionChannelHolder, position: Int) {
        holder.bind(channelList[position])
    }

    inner class ActionChannelHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(channelModel: ChannelModel) = with(itemView) {
            channelHeader.text = channelModel.name
        }
    }

    interface ActionChannelListener {
        fun onTurnOnActionChange(isChecked: Boolean, channelModel: ChannelModel)
        fun onTurnOffActionChange(isChecked: Boolean, channelModel: ChannelModel)
        fun onBrightnessChange(isChecked: Boolean, channelModel: ChannelModel)
        fun onStartOverflowChange(isChecked: Boolean, channelModel: ChannelModel)
        fun onStopOverflowChange(isChecked: Boolean, channelModel: ChannelModel)
    }
}
