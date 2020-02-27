package com.enxy.noolite.features.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enxy.noolite.R
import com.enxy.noolite.features.model.GroupModel
import kotlinx.android.synthetic.main.item_group.view.*


class GroupAdapter(private val listener: GroupListener) :
    RecyclerView.Adapter<GroupAdapter.GroupHolder>() {
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
            itemView.groupLinearLayout.setOnClickListener { listener.onGroupOpen(groupModel) }
            itemView.turnOnLightButton.setOnClickListener { listener.onTurnOnLights(groupModel) }
            itemView.turnOffLightButton.setOnClickListener { listener.onTurnOffLights(groupModel) }
            itemView.headerTextView.text = groupModel.name
            itemView.secondaryTextView.text = groupModel.channelElementsToString()
        }
    }

    interface GroupListener {
        fun onGroupOpen(groupModel: GroupModel)
        fun onTurnOnLights(groupModel: GroupModel)
        fun onTurnOffLights(groupModel: GroupModel)
    }
}
