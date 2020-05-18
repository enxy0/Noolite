package com.enxy.noolite.features.main.create

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.enxy.noolite.R
import com.enxy.noolite.core.base.BaseFragment
import com.enxy.noolite.core.data.Action
import com.enxy.noolite.core.data.Channel
import com.enxy.noolite.core.data.Group
import com.enxy.noolite.core.data.Script
import com.enxy.noolite.core.utils.extension.observe
import com.enxy.noolite.features.MainViewModel
import kotlinx.android.synthetic.main.fragment_script_group.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ActionGroupFragment : BaseFragment(), ActionChannelAdapter.ActionListener {
    private val viewModel: MainViewModel by sharedViewModel()
    private val actionGroupAdapter: ActionGroupAdapter = ActionGroupAdapter(this)
    private val script: Script =
        Script("", ArrayList())
    override val layoutId: Int
        get() = R.layout.fragment_script_group

    companion object {
        const val TAG = "ActionGroupFragment"
        fun newInstance() = ActionGroupFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(viewModel) {
            observe(groupList, ::renderData)
//            TODO: Close fragment on failure and notify user?
//            failure(groupFailure, ::handleFailure)1
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionGroupList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = actionGroupAdapter
        }
        createScript.setOnClickListener {
            script.name = scriptName.text.toString()
            if (script.actionsList.isNotEmpty()) {
                if (script.name.isNotEmpty()) {
                    viewModel.addScript(script)
                    notify(R.string.script_notify_done)
                    close()
                } else {
                    showKeyboardOnView(scriptName)
                    notify(R.string.script_notify_empty_name)
                }
            } else {
                notify(R.string.script_notify_cancel)
                close()
            }
        }
        cancelCreation.setOnClickListener {
            notify(R.string.script_notify_cancel)
            close()
        }
    }

    private fun renderData(groupList: ArrayList<Group>?) {
        if (!groupList.isNullOrEmpty()) {
            actionGroupAdapter.updateData(groupList)
        }
    }

    override fun onTurnOnActionChange(isChecked: Boolean, group: Group) {
        if (isChecked)
            script.write(group, Action.TURN_ON)
        else
            script.remove(group, Action.TURN_ON)
    }

    override fun onTurnOnActionChange(isChecked: Boolean, channel: Channel) {
        if (isChecked)
            script.write(channel, Action.TURN_ON)
        else
            script.remove(channel, Action.TURN_ON)
    }

    override fun onTurnOffActionChange(isChecked: Boolean, group: Group) {
        if (isChecked) {
            script.write(group, Action.TURN_OFF)
        } else {
            script.remove(group, Action.TURN_OFF)
        }
    }

    override fun onTurnOffActionChange(isChecked: Boolean, channel: Channel) {
        if (isChecked) {
            script.write(channel, Action.TURN_OFF)
        } else {
            script.remove(channel, Action.TURN_OFF)
        }
    }

    override fun onBrightnessChange(
        isChecked: Boolean,
        channel: Channel,
        brightness: Int
    ) {
        if (isChecked) {
            script.write(channel, Action.CHANGE_BRIGHTNESS, brightness)
        } else {
            script.remove(channel, Action.CHANGE_BRIGHTNESS)
        }
    }

    override fun onStartOverflowChange(isChecked: Boolean, channel: Channel) {
        if (isChecked) {
            script.write(channel, Action.START_OVERFLOW)
        } else {
            script.remove(channel, Action.START_OVERFLOW)
        }
    }

    override fun onStopOverflowChange(isChecked: Boolean, channel: Channel) {
        if (isChecked) {
            script.write(channel, Action.STOP_OVERFLOW)
        } else {
            script.remove(channel, Action.STOP_OVERFLOW)
        }
    }
}