package com.enxy.noolite.features.main.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enxy.noolite.R
import com.enxy.noolite.core.data.Channel
import com.enxy.noolite.core.data.Group
import com.enxy.noolite.core.data.Script
import com.enxy.noolite.core.utils.extension.toggleVisibility
import kotlinx.android.synthetic.main.item_action_group.view.*

class ActionGroupAdapter(private val actionListener: ActionChannelAdapter.ActionListener) :
    RecyclerView.Adapter<ActionGroupAdapter.ActionGroupHolder>() {
    private lateinit var recyclerView: RecyclerView
    private val script: Script? = null
    private val groupList = ArrayList<Group>()

    fun updateData(groupList: ArrayList<Group>) {
        this.groupList.clear()
        this.groupList.addAll(groupList)
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
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
        fun bind(group: Group) = with(itemView) {
            setUpRecyclerView(group.channelList)
            groupHeader.text = group.name
            groupChannels.text = group.channelElementsToString()

            // Turn off action listeners
            turnOffAction.setOnClickListener {
                turnOffCheck.toggleVisibility()
                actionListener.onTurnOffActionChange(turnOffCheck.isVisible, group)
            }
            // Turn on action listeners
            turnOnAction.setOnClickListener {
                turnOnCheck.toggleVisibility()
                actionListener.onTurnOnActionChange(turnOnCheck.isVisible, group)
            }

            // Animate button rotation and show available actions
            groupLayout.setOnClickListener { toggleAdditionalContent() }
            additionalContentButton.setOnClickListener { toggleAdditionalContent() }
        }

        /**
         * Shows additional content with smooth animation
         */
        private fun toggleAdditionalContent() = with(itemView) {
            val animationSpeed = context.applicationContext
                .resources
                .getInteger(R.integer.expand_animation_speed)
                .toLong()
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

        /**
         * Set ups recycler view to display channel model list from group model
         */
        private fun setUpRecyclerView(channelList: ArrayList<Channel>) =
            with(itemView.actionChannelList) {
                val channelAdapter =
                    ActionChannelAdapter(
                        actionListener
                    )
                adapter = channelAdapter
                layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(false)
                channelAdapter.updateData(channelList)
            }

        /**
         * Bounds one switch to another. If the first switch is clicked and the second switch
         * is checked then we uncheck the second switch.
         */
        private fun boundSwitches(firstSwitch: Switch, secondSwitch: Switch) {
            firstSwitch.isChecked = !firstSwitch.isChecked
            if (firstSwitch.isChecked)
                secondSwitch.isChecked = false
        }
    }
}