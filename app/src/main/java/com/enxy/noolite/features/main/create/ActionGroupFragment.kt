package com.enxy.noolite.features.main.create

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.enxy.noolite.R
import com.enxy.noolite.core.extension.observe
import com.enxy.noolite.core.platform.BaseFragment
import com.enxy.noolite.features.MainViewModel
import com.enxy.noolite.features.model.Action
import com.enxy.noolite.features.model.Channel
import com.enxy.noolite.features.model.Group
import com.enxy.noolite.features.model.Script
import kotlinx.android.synthetic.main.fragment_script_group.*

class ActionGroupFragment : BaseFragment(), ActionChannelAdapter.ActionListener {
    override val layoutId: Int = R.layout.fragment_script_group
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var actionGroupAdapter: ActionGroupAdapter
    private val script: Script = Script("TestScript", ArrayList())

    companion object {
        const val TAG = "CreateScriptFragment"
        fun newInstance() =
            ActionGroupFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        finishScript.setOnClickListener {
            Log.d("ActionGroupFragment", "onViewCreated: script=$script")
            if (script.actionsList.isNotEmpty()) {
                viewModel.addScript(script)
                notify("Скрипт был успешно создан!")
            } else
                notify("Пустой сценарий был удален")
            parentFragmentManager.popBackStack()
        }
        actionGroupAdapter = ActionGroupAdapter(this)
        setUpRecyclerView()
        with(viewModel) {
            observe(groupList, ::renderData)
//            TODO: Close fragment on failure and notify user?
//            failure(groupFailure, ::handleFailure)1
        }
//        startAnimation()
    }

    private fun renderData(groupList: ArrayList<Group>?) {
        groupList?.let { actionGroupAdapter.updateData(it) }
    }

    private fun setUpRecyclerView() = with(actionGroupList) {
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = actionGroupAdapter
        setHasFixedSize(false)
//        layoutAnimation = AnimationUtils.loadLayoutAnimation(
//            requireContext(), R.anim.layout_animation_fall_down
//        )
    }

    private fun startAnimation() {
        // Get duration
        val resources = requireContext().resources
        val layoutDuration = resources.getInteger(R.integer.layout_anim_duration).toLong()
        val fabDuration = resources.getInteger(R.integer.fab_anim_duration).toLong()

        // Animate appear of layout
        ObjectAnimator.ofFloat(scriptParentLayout, View.ALPHA, 0f, 1f).apply {
            duration = layoutDuration
        }.start()

        // Animate appear of FAB button
        with(finishScript) {
            alpha = 0f
            scaleX = 0f
            scaleY = 0f
            animate()
                .setDuration(fabDuration)
                .scaleX(1f).scaleY(1f).alpha(1f)
                .setInterpolator(OvershootInterpolator())
                .start()
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
        if (isChecked)
            script.write(group, Action.TURN_OFF)
        else
            script.remove(group, Action.TURN_OFF)
    }

    override fun onTurnOffActionChange(isChecked: Boolean, channel: Channel) {
        if (isChecked)
            script.write(channel, Action.TURN_OFF)
        else
            script.remove(channel, Action.TURN_OFF)
    }

    override fun onBrightnessChange(
        isChecked: Boolean,
        channel: Channel,
        brightness: Int
    ) {
        if (isChecked)
            script.write(channel, Action.CHANGE_BRIGHTNESS, brightness)
        else
            script.remove(channel, Action.CHANGE_BRIGHTNESS)
    }

    override fun onStartOverflowChange(isChecked: Boolean, channel: Channel) {
        if (isChecked)
            script.write(channel, Action.START_OVERFLOW)
        else
            script.remove(channel, Action.START_OVERFLOW)
    }

    override fun onStopOverflowChange(isChecked: Boolean, channel: Channel) {
        if (isChecked)
            script.write(channel, Action.STOP_OVERFLOW)
        else
            script.remove(channel, Action.STOP_OVERFLOW)
    }
}