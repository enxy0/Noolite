package com.enxy.noolite.features.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.enxy.noolite.R
import com.enxy.noolite.features.MainViewModel
import com.enxy.noolite.features.model.ChannelModel
import com.enxy.noolite.features.model.GroupModel
import kotlinx.android.synthetic.main.item_default.view.*


class ChannelAdapter(private var viewModel: MainViewModel) :
    RecyclerView.Adapter<ChannelAdapter.ChannelHolderDefault>() {
    private var data: ArrayList<ChannelModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelHolderDefault {
        return ChannelHolderDefault(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_default,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ChannelHolderDefault, position: Int) {
        holder.bind(data[position])
    }

    fun addAll(groupModel: GroupModel) {
        this.data.addAll(groupModel.channelModelList)
    }

    fun clear() {
        this.data.clear()
    }

    inner class ChannelHolderDefault(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(channelModel: ChannelModel) {
            when (channelModel.type) {
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
                                viewModel.turnOnLight(channelModel.id)
                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            when (val brightness = seekBar!!.progress) {
                                0 -> viewModel.turnOffLight(channelModel.id)
                                else -> viewModel.changeBacklightBrightness(
                                    channelModel.id,
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
                            viewModel.turnOnLight(channelModel.id)
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        when (val brightness = seekBar!!.progress) {
                            0 -> viewModel.turnOffLight(channelModel.id)
                            else -> viewModel.changeBacklightBrightness(channelModel.id, brightness)
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
                viewModel.changeLightState(channelModel.id)
            }

            itemView.turnOnLightButton.setOnClickListener {
                viewModel.turnOnLight(channelModel.id)
            }

            itemView.turnOffLightButton.setOnClickListener {
                viewModel.turnOffLight(channelModel.id)
            }

            itemView.startOverflowButton.setOnClickListener {
                viewModel.startBacklightOverflow(channelModel.id)
            }

            itemView.stopOverflowButton.setOnClickListener {
                viewModel.stopBacklightOverflow(channelModel.id)
            }

            itemView.changeColorButton.setOnClickListener {
                viewModel.changeBacklightColor(channelModel.id)
            }

            if (!viewModel.settingsManager.hasToggleButton) {
                itemView.toggleLightButton.visibility = View.GONE
            } else {
                itemView.turnOnLightButton.visibility = View.GONE
                itemView.turnOffLightButton.visibility = View.GONE
            }
            itemView.headerTextView.text = channelModel.name
        }
    }
}