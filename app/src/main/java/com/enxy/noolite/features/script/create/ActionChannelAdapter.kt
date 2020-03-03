package com.enxy.noolite.features.script.create

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.enxy.noolite.R
import com.enxy.noolite.core.extension.toggleVisibility
import com.enxy.noolite.features.model.Channel
import com.enxy.noolite.features.model.Group
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

            // Animate button rotation and show additional content
            channelLayout.setOnClickListener { toggleAdditionalContent() }
            additionalContentButton.setOnClickListener { toggleAdditionalContent() }

            // Light section
            turnOnAction.setOnClickListener {
                turnOnCheck.toggleVisibility()
                listener.onTurnOnActionChange(turnOnCheck.isVisible, channel)
            }
            turnOffAction.setOnClickListener {
                turnOffCheck.toggleVisibility()
                listener.onTurnOffActionChange(turnOffCheck.isVisible, channel)
            }

            // Brightness section
            changeBrightnessAction.setOnClickListener {
                changeBrightnessCheck.toggleVisibility()
                listener.onBrightnessChange(
                    changeBrightnessCheck.isVisible,
                    channel,
                    indicatorSeekBar.progress
                )
            }

            indicatorSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    val value = "${progress}%"
                    progressValue.text = value
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    if (!changeBrightnessCheck.isVisible) {
                        changeBrightnessCheck.toggleVisibility()
                    }
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.let {
                        listener.onBrightnessChange(
                            changeBrightnessCheck.isVisible,
                            channel,
                            seekBar.progress
                        )
                    }
                }
            })

            // Overflow section
            startOverflowAction.setOnClickListener {
                startOverflowCheck.toggleVisibility()
                listener.onStartOverflowChange(startOverflowCheck.isVisible, channel)
            }
            stopOverflowAction.setOnClickListener {
                stopOverflowCheck.toggleVisibility()
                listener.onStopOverflowChange(stopOverflowCheck.isVisible, channel)
            }
        }

        private fun boundViews(first: View, second: View) {
            first.toggleVisibility()
            if (second.isVisible)
                second.toggleVisibility()
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
        fun onTurnOnActionChange(isChecked: Boolean, group: Group)
        fun onTurnOffActionChange(isChecked: Boolean, group: Group)
        fun onTurnOnActionChange(isChecked: Boolean, channel: Channel)
        fun onTurnOffActionChange(isChecked: Boolean, channel: Channel)
        fun onBrightnessChange(isChecked: Boolean, channel: Channel, brightness: Int)
        fun onStartOverflowChange(isChecked: Boolean, channel: Channel)
        fun onStopOverflowChange(isChecked: Boolean, channel: Channel)
    }
}
