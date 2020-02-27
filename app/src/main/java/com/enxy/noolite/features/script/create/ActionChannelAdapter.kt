package com.enxy.noolite.features.script.create

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.enxy.noolite.R
import com.enxy.noolite.features.model.ChannelModel
import com.enxy.noolite.features.model.GroupModel
import kotlinx.android.synthetic.main.item_action_channel.view.*

class ActionChannelAdapter(private val listener: ActionListener) :
    RecyclerView.Adapter<ActionChannelAdapter.ActionChannelHolder>() {
    private val channelList = ArrayList<ChannelModel>()
    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

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

            // Animate button rotation and show additional content
            channelLayout.setOnClickListener { toggleAdditionalContent() }
            additionalContentButton.setOnClickListener { toggleAdditionalContent() }
        }

        private fun toggleAdditionalContent() = with(itemView) {
            val animationSpeed = context.applicationContext.resources
                .getInteger(R.integer.expand_animation_speed)
                .toLong()
            val autoTransition = AutoTransition().apply {
                duration = animationSpeed
            }
            TransitionManager.beginDelayedTransition(recyclerView, autoTransition)
            if (additionalContent.isVisible) {
                additionalContentButton.animate()
                    .setDuration(animationSpeed)
                    .rotation(0f)
                    .start()
                additionalContent.visibility = View.GONE
            } else {
                additionalContent.visibility = View.VISIBLE
                additionalContentButton.animate()
                    .setDuration(animationSpeed)
                    .rotation(180f)
                    .start()
            }
        }
    }

    interface ActionListener {
        fun onTurnOnActionChange(isChecked: Boolean, groupModel: GroupModel)
        fun onTurnOffActionChange(isChecked: Boolean, groupModel: GroupModel)
        fun onTurnOnActionChange(isChecked: Boolean, channelModel: ChannelModel)
        fun onTurnOffActionChange(isChecked: Boolean, channelModel: ChannelModel)
        fun onBrightnessChange(isChecked: Boolean, channelModel: ChannelModel)
        fun onStartOverflowChange(isChecked: Boolean, channelModel: ChannelModel)
        fun onStopOverflowChange(isChecked: Boolean, channelModel: ChannelModel)
    }
}
