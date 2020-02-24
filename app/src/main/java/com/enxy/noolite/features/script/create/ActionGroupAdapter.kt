package com.enxy.noolite.features.script.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.enxy.noolite.R
import com.enxy.noolite.features.model.GroupModel
import kotlinx.android.synthetic.main.item_action_group.view.*

class ActionGroupAdapter(private val actionListener: ActionListener) :
    RecyclerView.Adapter<ActionGroupAdapter.ActionGroupHolder>() {
    private val groupList = ArrayList<GroupModel>()

    fun updateData(groupList: ArrayList<GroupModel>) {
        this.groupList.clear()
        this.groupList.addAll(groupList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionGroupHolder {
        val view =
            LayoutInflater.from(parent.context!!).inflate(R.layout.item_action_group, parent, false)
        return ActionGroupHolder(view)
    }

    override fun getItemCount(): Int = groupList.size

    override fun onBindViewHolder(holder: ActionGroupHolder, position: Int) {
        holder.bind(groupList[position])
    }

    inner class ActionGroupHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(groupModel: GroupModel) = with(itemView) {
            groupHeader.text = groupModel.name
            groupChannels.text = groupModel.channelElementsToString()
            groupLayout.setOnClickListener { actionListener.onOpenGroup(groupModel) }

            // Turn off action listeners
            turnOffAction.setOnClickListener {
                boundSwitches(turnOffSwitch, turnOnSwitch)
                actionListener.onTurnOffActionChange(turnOffSwitch.isChecked, groupModel)
            }
            turnOffSwitch.setOnCheckedChangeListener { _, isChecked ->
                actionListener.onTurnOffActionChange(isChecked, groupModel)
                if (isChecked)
                    turnOnSwitch.isChecked = false
            }

            // Turn on action listeners
            turnOnAction.setOnClickListener {
                boundSwitches(turnOnSwitch, turnOffSwitch)
                actionListener.onTurnOnActionChange(turnOffSwitch.isChecked, groupModel)
            }
            turnOnSwitch.setOnCheckedChangeListener { _, isChecked ->
                actionListener.onTurnOnActionChange(isChecked, groupModel)
                if (isChecked)
                    turnOffSwitch.isChecked = false
            }

            // Animate button rotation and show additional content
            additionalContent.setOnClickListener {
                if (actionsContainer.isVisible) {
                    actionsContainer.visibility = View.GONE
                    additionalContent.animate().setDuration(200L).rotation(0f).start()
                } else {
                    additionalContent.animate().setDuration(200L).rotation(180f).start()
                    actionsContainer.visibility = View.VISIBLE
                }
            }
        }

        private fun boundSwitches(firstSwitch: Switch, secondSwitch: Switch) {
            firstSwitch.isChecked = !firstSwitch.isChecked
            if (firstSwitch.isChecked)
                secondSwitch.isChecked = false
        }
    }

    interface ActionListener {
        fun onTurnOnActionChange(isChecked: Boolean, groupModel: GroupModel)
        fun onTurnOffActionChange(isChecked: Boolean, groupModel: GroupModel)
        fun onOpenGroup(groupModel: GroupModel)
    }
}