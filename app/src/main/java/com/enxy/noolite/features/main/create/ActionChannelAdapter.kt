package com.enxy.noolite.features.main.create

import android.transition.AutoTransition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.enxy.noolite.R
import com.enxy.noolite.core.data.Channel
import com.enxy.noolite.core.data.Group
import com.enxy.noolite.core.utils.extension.toggleVisibility
import kotlinx.android.synthetic.main.item_action_channel.view.*

class ActionChannelAdapter(private val listener: ActionListener) :
    RecyclerView.Adapter<ActionChannelAdapter.ActionChannelHolder>() {
    private val channelList = ArrayList<Channel>()
    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun updateData(channelList: ArrayList<Channel>) {
        this.channelList.clear()
        this.channelList.addAll(channelList)
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
        fun bind(channel: Channel) = with(itemView) {
            channelHeader.text = channel.name
            when (channel.type) {
                0 -> {
                    brightnessSection.isGone = true
                    overflowSection.isGone = true
                }
                1 -> {
                    overflowSection.isGone = true
                    setUpBrightnessSection(channel)
                }
                3 -> {
                    setUpBrightnessSection(channel)
                    setUpOverflowSection(channel)
                }
            }

            // Animate button rotation and show additional content
            channelLayout.setOnClickListener { toggleAdditionalContent() }
            additionalContentButton.setOnClickListener { toggleAdditionalContent() }

            turnOnAction.setOnClickListener {
                turnOnCheck.toggleVisibility()
                listener.onTurnOnActionChange(turnOnCheck.isVisible, channel)
            }
            turnOffAction.setOnClickListener {
                turnOffCheck.toggleVisibility()
                listener.onTurnOffActionChange(turnOffCheck.isVisible, channel)
            }
        }

        private fun setUpBrightnessSection(channel: Channel) = with(itemView) {
            indicatorSeekBar.setOnSeekBarChangeListener(getSeekBarListener(channel))
            changeBrightnessAction.setOnClickListener {
                changeBrightnessCheck.toggleVisibility()
                listener.onBrightnessChange(
                    changeBrightnessCheck.isVisible,
                    channel,
                    indicatorSeekBar.progress
                )
            }
        }

        private fun setUpOverflowSection(channel: Channel) = with(itemView) {
            startOverflowAction.setOnClickListener {
                startOverflowCheck.toggleVisibility()
                listener.onStartOverflowChange(startOverflowCheck.isVisible, channel)
            }
            stopOverflowAction.setOnClickListener {
                stopOverflowCheck.toggleVisibility()
                listener.onStopOverflowChange(stopOverflowCheck.isVisible, channel)
            }
        }

        private fun getSeekBarListener(channel: Channel): SeekBar.OnSeekBarChangeListener =
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    val value = "${progress}%"
                    itemView.progressValue.text = value
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    if (!itemView.changeBrightnessCheck.isVisible) {
                        itemView.changeBrightnessCheck.toggleVisibility()
                    }
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.let {
                        val isChecked = itemView.changeBrightnessCheck.isVisible
                        listener.onBrightnessChange(isChecked, channel, seekBar.progress)
                    }
                }
            }

        private fun toggleAdditionalContent() = with(itemView) {
            val animationSpeed = context.applicationContext.resources
                .getInteger(R.integer.expand_animation_speed)
                .toLong()
            val autoTransition = AutoTransition().apply {
                duration = animationSpeed
            }
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
        fun onTurnOnActionChange(isChecked: Boolean, group: Group)
        fun onTurnOffActionChange(isChecked: Boolean, group: Group)
        fun onTurnOnActionChange(isChecked: Boolean, channel: Channel)
        fun onTurnOffActionChange(isChecked: Boolean, channel: Channel)
        fun onBrightnessChange(isChecked: Boolean, channel: Channel, brightness: Int)
        fun onStartOverflowChange(isChecked: Boolean, channel: Channel)
        fun onStopOverflowChange(isChecked: Boolean, channel: Channel)
    }
}
