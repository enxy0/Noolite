package com.enxy.noolite.features.channel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.enxy.noolite.R
import com.enxy.noolite.core.data.Channel
import kotlinx.android.synthetic.main.item_channel.view.*


class ChannelAdapter(private val listener: ChannelListener) :
    RecyclerView.Adapter<ChannelAdapter.ChannelHolder>() {
    private var hasToggleButton: Boolean = true
    private var data: ArrayList<Channel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelHolder {
        return ChannelHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_channel,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ChannelHolder, position: Int) {
        holder.bind(data[position])
    }

    fun clear() {
        this.data.clear()
        notifyDataSetChanged()
    }

    fun updateData(channelList: ArrayList<Channel>) {
        this.data.clear()
        this.data.addAll(channelList)
        notifyDataSetChanged()
    }

    fun setToggleButtonVisibility(hasToggleButton: Boolean) {
        this.hasToggleButton = hasToggleButton
        notifyDataSetChanged()
    }

    inner class ChannelHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(channel: Channel) = with(itemView) {
            headerTextView.text = channel.name
            when (channel.type) {
                0 -> {
                    brightnessLinearLayout.visibility = View.GONE
                    overflowLinearLayout.visibility = View.GONE
                }
                1 -> {
                    indicatorSeekBar.setOnSeekBarChangeListener(getSeekBarListener(channel))
                    overflowLinearLayout.visibility = View.GONE
                }
                3 -> {
                    indicatorSeekBar.setOnSeekBarChangeListener(getSeekBarListener(channel))
                }
            }

            toggleLightButton.setOnClickListener { listener.onChangeState(channel) }
            turnOnLightButton.setOnClickListener { listener.onTurnOnLight(channel) }
            turnOffLightButton.setOnClickListener { listener.onTurnOffLight(channel) }
            startOverflowButton.setOnClickListener { listener.onStartOverflow(channel) }
            stopOverflowButton.setOnClickListener { listener.onStopOverflow(channel) }
            changeColorButton.setOnClickListener { listener.onChangeBacklightColor(channel) }

            if (hasToggleButton) {
                turnOnLightButton.visibility = View.GONE
                turnOffLightButton.visibility = View.GONE
            } else {
                toggleLightButton.visibility = View.GONE
            }
        }

        private fun getSeekBarListener(channel: Channel): SeekBar.OnSeekBarChangeListener =
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) = Unit

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    if (seekBar!!.progress == 0)
                        listener.onTurnOnLight(channel)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) =
                    when (val brightness = seekBar!!.progress) {
                        0 -> listener.onTurnOffLight(channel)
                        else -> listener.onChangeBrightness(channel, brightness)
                    }
            }
    }

    interface ChannelListener {
        fun onTurnOnLight(channel: Channel)
        fun onTurnOffLight(channel: Channel)
        fun onChangeState(channel: Channel)
        fun onChangeBrightness(channel: Channel, brightness: Int)
        fun onStartOverflow(channel: Channel)
        fun onStopOverflow(channel: Channel)
        fun onChangeBacklightColor(channel: Channel)
    }
}