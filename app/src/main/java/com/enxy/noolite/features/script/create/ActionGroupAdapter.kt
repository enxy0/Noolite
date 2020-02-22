package com.enxy.noolite.features.script.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            groupLayout.setOnClickListener {
                actionListener.onOpenGroup(groupModel)
            }
            turnOffAction.setOnClickListener {
                turnOffCheckbox.isChecked = !turnOffCheckbox.isChecked
                if (turnOffCheckbox.isChecked)
                    turnOnCheckbox.isChecked = false
                actionListener.onTurnOffActionChange(turnOffCheckbox.isChecked, groupModel)
            }
            turnOffCheckbox.setOnCheckedChangeListener { _, isChecked ->
                actionListener.onTurnOffActionChange(isChecked, groupModel)
                if (isChecked)
                    turnOnCheckbox.isChecked = false
            }

            turnOnAction.setOnClickListener {
                turnOnCheckbox.isChecked = !turnOnCheckbox.isChecked
                if (turnOnCheckbox.isChecked)
                    turnOffCheckbox.isChecked = false
                actionListener.onTurnOnActionChange(turnOffCheckbox.isChecked, groupModel)
            }
            turnOnCheckbox.setOnCheckedChangeListener { _, isChecked ->
                turnOnCheckbox.isChecked = isChecked
                actionListener.onTurnOnActionChange(isChecked, groupModel)
                if (isChecked)
                    turnOffCheckbox.isChecked = false
            }

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
    }

    interface ActionListener {
        fun onTurnOnActionChange(isChecked: Boolean, groupModel: GroupModel)
        fun onTurnOffActionChange(isChecked: Boolean, groupModel: GroupModel)
        fun onOpenGroup(groupModel: GroupModel)
    }
}