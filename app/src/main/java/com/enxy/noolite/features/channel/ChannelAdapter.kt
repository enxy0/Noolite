package com.enxy.noolite.features.channel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.enxy.noolite.R
import com.enxy.noolite.features.MainViewModel
import com.enxy.noolite.features.model.Channel
import kotlinx.android.synthetic.main.item_channel.view.*


class ChannelAdapter(private var viewModel: MainViewModel) :
    RecyclerView.Adapter<ChannelAdapter.ChannelHolderDefault>() {
    private var data: ArrayList<Channel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelHolderDefault {
        return ChannelHolderDefault(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_channel,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ChannelHolderDefault, position: Int) {
        holder.bind(data[position])
    }

    fun updateData(channelList: ArrayList<Channel>) {
        this.data.clear()
        this.data.addAll(channelList)
        notifyDataSetChanged()
    }

    inner class ChannelHolderDefault(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(channel: Channel) {
            when (channel.type) {
                0 -> {
                    itemView.brightnessLinearLayout.visibility = View.GONE
                    itemView.overflowLinearLayout.visibility = View.GONE
                }

                1 -> {
                    itemView.overflowLinearLayout.visibility = View.GONE
                    itemView.indicatorSeekBar.setOnSeekBarChangeListener(object :
                        SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar?,
                            progress: Int,
                            fromUser: Boolean
                        ) {
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            if (seekBar!!.progress == 0)
                                viewModel.turnOnLight(channel.id)
                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            when (val brightness = seekBar!!.progress) {
                                0 -> viewModel.turnOffLight(channel.id)
                                else -> viewModel.changeBacklightBrightness(
                                    channel.id,
                                    brightness
                                )
                            }
                            itemView.overflowLinearLayout.visibility = View.GONE
                        }
                    })
                }

                3 -> itemView.indicatorSeekBar.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        if (seekBar!!.progress == 0)
                            viewModel.turnOnLight(channel.id)
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        when (val brightness = seekBar!!.progress) {
                            0 -> viewModel.turnOffLight(channel.id)
                            else -> viewModel.changeBacklightBrightness(channel.id, brightness)
                        }
                    }

                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                    }
                })
            }

            itemView.toggleLightButton.setOnClickListener {
                viewModel.changeLightState(channel.id)
            }

            itemView.turnOnLightButton.setOnClickListener {
                viewModel.turnOnLight(channel.id)
            }

            itemView.turnOffLightButton.setOnClickListener {
                viewModel.turnOffLight(channel.id)
            }

            itemView.startOverflowButton.setOnClickListener {
                viewModel.startBacklightOverflow(channel.id)
            }

            itemView.stopOverflowButton.setOnClickListener {
                viewModel.stopBacklightOverflow(channel.id)
            }

            itemView.changeColorButton.setOnClickListener {
                viewModel.changeBacklightColor(channel.id)
            }

            if (!viewModel.settingsManager.hasToggleButton) {
                itemView.toggleLightButton.visibility = View.GONE
            } else {
                itemView.turnOnLightButton.visibility = View.GONE
                itemView.turnOffLightButton.visibility = View.GONE
            }
            itemView.headerTextView.text = channel.name
        }
    }
}