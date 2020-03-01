package com.enxy.noolite.features.script.create

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import com.enxy.noolite.R
import com.enxy.noolite.core.extension.getActivityViewModel
import com.enxy.noolite.core.extension.observe
import com.enxy.noolite.core.platform.BaseFragment
import com.enxy.noolite.features.MainViewModel
import com.enxy.noolite.features.model.ChannelModel
import com.enxy.noolite.features.model.GroupModel
import kotlinx.android.synthetic.main.fragment_script_group.*

class ActionGroupFragment : BaseFragment(), ActionChannelAdapter.ActionListener {
    override val layoutId: Int = R.layout.fragment_script_group
    private lateinit var viewModel: MainViewModel
    private lateinit var actionGroupAdapter: ActionGroupAdapter

    companion object {
        const val TAG = "CreateScriptFragment"
        fun newInstance() = ActionGroupFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        finishScript.setOnClickListener { parentFragmentManager.popBackStack() }
        viewModel = getActivityViewModel(this)
        actionGroupAdapter = ActionGroupAdapter(this)
        setUpRecyclerView()
        with(viewModel) {
            observe(groupElementList, ::renderData)
//            TODO: Close fragment on failure and notify user?
//            failure(groupFailure, ::handleFailure)
        }
//        startAnimation()
    }

    private fun renderData(groupList: ArrayList<GroupModel>?) {
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

    override fun onTurnOnActionChange(isChecked: Boolean, groupModel: GroupModel) {
        Log.d(
            "ActionGroupFragment",
            "onTurnOnActionChange: isChecked=$isChecked, groupModel=$groupModel"
        )
    }

    override fun onTurnOnActionChange(isChecked: Boolean, channelModel: ChannelModel) {
        Log.d(
            "ActionGroupFragment",
            "onTurnOnActionChange: isChecked=$isChecked, channelModel=$channelModel"
        )
    }

    override fun onTurnOffActionChange(isChecked: Boolean, groupModel: GroupModel) {
        Log.d(
            "ActionGroupFragment",
            "onTurnOffActionChange: isChecked=$isChecked, groupModel=$groupModel"
        )
    }

    override fun onTurnOffActionChange(isChecked: Boolean, channelModel: ChannelModel) {
        Log.d(
            "ActionGroupFragment",
            "onTurnOffActionChange: isChecked=$isChecked, channelModel=$channelModel"
        )
    }

    override fun onBrightnessChange(isChecked: Boolean, channelModel: ChannelModel) {
        Log.d(
            "ActionGroupFragment",
            "onBrightnessChange: isChecked=$isChecked, channelModel=$channelModel"
        )
    }

    override fun onStartOverflowChange(isChecked: Boolean, channelModel: ChannelModel) {
        Log.d(
            "ActionGroupFragment",
            "onStartOverflowChange: isChecked=$isChecked, channelModel=$channelModel"
        )
    }

    override fun onStopOverflowChange(isChecked: Boolean, channelModel: ChannelModel) {
        Log.d(
            "ActionGroupFragment",
            "onStopOverflowChange: isChecked=$isChecked, channelModel=$channelModel"
        )
    }
}