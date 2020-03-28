package com.enxy.noolite.features.channel

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.enxy.noolite.R
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.extension.failure
import com.enxy.noolite.core.extension.observe
import com.enxy.noolite.core.platform.BaseFragment
import com.enxy.noolite.features.MainViewModel
import com.enxy.noolite.features.model.Channel
import com.enxy.noolite.features.model.ChannelAction
import com.enxy.noolite.features.model.Group
import kotlinx.android.synthetic.main.fragment_channel.*


class ChannelFragment : BaseFragment(), ChannelAdapter.ChannelListener {
    private val channelAdapter: ChannelAdapter = ChannelAdapter(this)
    private val viewModel: MainViewModel by activityViewModels()
    private val passedGroup: Group
        get() = requireArguments().getSerializable(GROUP_MODEL_KEY) as Group

    override val layoutId = R.layout.fragment_channel

    companion object {
        fun newInstance(group: Group) = ChannelFragment().apply {
            arguments = Bundle().apply { putSerializable(GROUP_MODEL_KEY, group) }
        }

        fun newInstance() = ChannelFragment()

        const val GROUP_MODEL_KEY = "group_model"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(viewModel) {
            observe(favouriteGroup, ::handleFavouriteGroup)
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        channelAdapter.setToggleButtonVisibility(viewModel.hasToggleButton)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        with(channelRecyclerView) {
            adapter = channelAdapter
            layoutManager = linearLayoutManager
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()
//            showBackButton()
        setToolbarTitle(passedGroup.name)
    }

    private fun handleFavouriteGroup(favouriteGroup: Group?) {
        favouriteGroup?.let {
            renderGroupElement(passedGroup)
            if (favouriteGroup != passedGroup)
                showAddToFavourite()
        }
    }

    private fun handleFailure(failure: Failure?) {
        failure?.let {
            renderGroupElement(passedGroup)
            showAddToFavourite()
        }
    }

    private fun renderGroupElement(group: Group) {
        if (errorLayout.isVisible) {
            channelRecyclerView.isVisible = true
            errorLayout.isGone = true
        }
        groupName.text = group.name
        channelAdapter.updateData(group.channelList)
    }

    private fun showAddToFavourite() {
        favouriteButton.visibility = View.VISIBLE
        favouriteButton.setOnClickListener {
            viewModel.setFavouriteGroup(passedGroup)
            notify(R.string.message_room_added_as_favourite)
        }
    }

    override fun onTurnOnLight(channel: Channel) {
        viewModel.doAction(ChannelAction.createTurnOnAction(channel.id))
    }

    override fun onTurnOffLight(channel: Channel) {
        viewModel.doAction(ChannelAction.createTurnOffAction(channel.id))
    }

    override fun onChangeState(channel: Channel) {
        viewModel.doAction(ChannelAction.createChangeStateAction(channel.id))
    }

    override fun onChangeBrightness(channel: Channel, brightness: Int) {
        viewModel.doAction(ChannelAction.createChangeBrightnessAction(channel.id, brightness))
    }

    override fun onStartOverflow(channel: Channel) {
        viewModel.doAction(ChannelAction.createStartOverflowAction(channel.id))
    }

    override fun onStopOverflow(channel: Channel) {
        viewModel.doAction(ChannelAction.createStopOverflowAction(channel.id))
    }

    override fun onChangeBacklightColor(channel: Channel) {
        viewModel.doAction(ChannelAction.createChangeColorAction(channel.id))
    }
}