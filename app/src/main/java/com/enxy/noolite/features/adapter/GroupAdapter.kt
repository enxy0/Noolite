package com.enxy.noolite.features.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.enxy.noolite.R
import com.enxy.noolite.features.ChannelFragment
import com.enxy.noolite.features.GroupFragment
import com.enxy.noolite.features.MainViewModel
import com.enxy.noolite.features.model.GroupModel
import kotlinx.android.synthetic.main.item_group.view.*


class GroupAdapter(private val fragment: GroupFragment, private val viewModel: MainViewModel) : RecyclerView.Adapter<GroupAdapter.GroupHolder>() {
    private var data: ArrayList<GroupModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        return GroupHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false))
    }

    override fun getItemCount(): Int = this.data.size

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        holder.bind(data[position])
    }

    fun addAll(data: ArrayList<GroupModel>) {
        this.data.addAll(data)
    }

    fun clear() {
        this.data.clear()
    }

    inner class GroupHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(groupModel: GroupModel) {
            itemView.groupLinearLayout.setOnClickListener {
                viewModel.chosenGroupElement.value = groupModel
                fragment.parentFragmentManager.commit {
                    addToBackStack(null)
                    setCustomAnimations(
                        R.anim.zoom_in,
                        R.anim.zoom_out,
                        R.anim.parent_zoom_in,
                        R.anim.parent_zoom_out
                    )
                    replace(R.id.fragmentHolder, ChannelFragment.newInstance())
                }
            }
            itemView.turnOnLightButton.setOnClickListener {
                for (channel in groupModel.channelModelList)
                    viewModel.turnOnLight(channel.id)
            }

            itemView.turnOffLightButton.setOnClickListener {
                for (channel in groupModel.channelModelList)
                    viewModel.turnOffLight(channel.id)
            }

            itemView.headerTextView.text = groupModel.name
            itemView.secondaryTextView.text = groupModel.channelElementsToString()
        }
    }
}
