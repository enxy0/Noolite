package com.enxy.noolite.features.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enxy.noolite.R
import com.enxy.noolite.core.data.Group
import kotlinx.android.synthetic.main.item_group.view.*


class GroupAdapter(private val listener: GroupListener) :
    RecyclerView.Adapter<GroupAdapter.GroupHolder>() {
    private var data: ArrayList<Group> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupHolder(view)
    }

    override fun getItemCount(): Int = this.data.size

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        holder.bind(data[position])
    }

    fun updateData(data: ArrayList<Group>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class GroupHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(group: Group) = with(itemView) {
            groupLinearLayout.setOnClickListener { listener.onGroupOpen(group) }
            turnOnLightButton.setOnClickListener { listener.onTurnOnLights(group) }
            turnOffLightButton.setOnClickListener { listener.onTurnOffLights(group) }
            headerTextView.text = group.name
            secondaryTextView.text = group.channelElementsToString()
        }
    }

    interface GroupListener {
        fun onGroupOpen(group: Group)
        fun onTurnOnLights(group: Group)
        fun onTurnOffLights(group: Group)
    }
}
