package com.enxy.noolite.features.script.create

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.enxy.noolite.R
import com.enxy.noolite.core.extension.getActivityViewModel
import com.enxy.noolite.core.platform.BaseFragment
import com.enxy.noolite.features.MainViewModel
import com.enxy.noolite.features.model.ChannelModel
import com.enxy.noolite.features.model.GroupModel
import kotlinx.android.synthetic.main.fragment_script_channel.*

class ActionChannelFragment : BaseFragment(), ActionChannelAdapter.ActionChannelListener {
    override val layoutId: Int = R.layout.fragment_script_channel
    private lateinit var viewModel: MainViewModel
    private lateinit var actionAdapter: ActionChannelAdapter

    companion object {
        const val TAG = "ActionChannelFragment"
        fun newInstance(groupModel: GroupModel) = ActionChannelFragment().apply {
            arguments = Bundle().apply { putSerializable(GROUP_MODEL_KEY, groupModel) }
        }

        const val GROUP_MODEL_KEY = "group_model"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getActivityViewModel(this)
        val groupModel = getGroupModel()
        Log.d("ActionChannelFragment", "onViewCreated: groupModel=$groupModel")
        actionAdapter = ActionChannelAdapter(this)
        with(actionChannelList) {
            adapter = actionAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )
            setHasFixedSize(true)
        }
        actionAdapter.updateData(groupModel.channelModelList)
    }

    private fun getGroupModel() = arguments!!.getSerializable(GROUP_MODEL_KEY) as GroupModel

    override fun onTurnOnActionChange(isChecked: Boolean, channelModel: ChannelModel) {
        Log.d(
            "ActionChannelFragment",
            "onTurnOnActionChange: isChecked=$isChecked, channelModel=$channelModel"
        )
    }

    override fun onTurnOffActionChange(isChecked: Boolean, channelModel: ChannelModel) {
        Log.d(
            "ActionChannelFragment",
            "onTurnOffActionChange: isChecked=$isChecked, channelModel=$channelModel"
        )
    }

    override fun onBrightnessChange(isChecked: Boolean, channelModel: ChannelModel) {
        Log.d(
            "ActionChannelFragment",
            "onBrightnessChange: isChecked=$isChecked, channelModel=$channelModel"
        )
    }

    override fun onStartOverflowChange(isChecked: Boolean, channelModel: ChannelModel) {
        Log.d(
            "ActionChannelFragment",
            "onStartOverflowChange: isChecked=$isChecked, channelModel=$channelModel"
        )
    }

    override fun onStopOverflowChange(isChecked: Boolean, channelModel: ChannelModel) {
        Log.d(
            "ActionChannelFragment",
            "onStopOverflowChange: isChecked=$isChecked, channelModel=$channelModel"
        )
    }
}