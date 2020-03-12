package com.enxy.noolite.features.channel

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.enxy.noolite.R
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.extension.failure
import com.enxy.noolite.core.extension.getViewModel
import com.enxy.noolite.core.extension.observe
import com.enxy.noolite.core.platform.BaseFragment
import com.enxy.noolite.features.MainViewModel
import com.enxy.noolite.features.model.Channel
import com.enxy.noolite.features.model.ChannelAction
import com.enxy.noolite.features.model.Group
import kotlinx.android.synthetic.main.fragment_channel.*
import javax.inject.Inject


class ChannelFragment : BaseFragment(), ChannelAdapter.ChannelListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var channelAdapter: ChannelAdapter
    private val activityViewModel: MainViewModel by activityViewModels()
    private lateinit var viewModel: ChannelViewModel
    private val passedGroup: Group
        get() = requireArguments().getSerializable(GROUP_MODEL_KEY) as Group
    private val hasPassedData: Boolean
        get() = arguments != null

    override val layoutId = R.layout.fragment_channel

    companion object {
        fun newInstance(group: Group) = ChannelFragment().apply {
            arguments = Bundle().apply { putSerializable(GROUP_MODEL_KEY, group) }
        }

        fun newInstance() = ChannelFragment()

        const val GROUP_MODEL_KEY = "group_model"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        viewModel = getViewModel(this, viewModelFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        with(activityViewModel) {
            observe(favouriteGroup, ::handleFavouriteGroup)
            failure(favouriteGroupFailure, ::handleFailure)
        }
    }

    override fun onResume() {
        super.onResume()
        if (hasPassedData)
            setUpBackButton()
    }

    private fun handleFavouriteGroup(favouriteGroup: Group?) {
        favouriteGroup?.let {
            if (hasPassedData) {
                renderGroupElement(passedGroup)
                if (favouriteGroup != passedGroup)
                    showAddToFavourite()
            } else
                renderGroupElement(favouriteGroup)
        }
    }

    private fun handleFailure(failure: Failure?) {
        failure?.let {
            if (hasPassedData) {
                renderGroupElement(passedGroup)
                showAddToFavourite()
            } else {
                if (!errorLayout.isVisible) {
                    channelRecyclerView.isGone = true
                    errorLayout.isVisible = true
                }
            }
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

    override fun onPause() {
        super.onPause()
        hideBackButton()
    }

    private fun setUpRecyclerView() {
        channelAdapter = ChannelAdapter(this, viewModel.hasToggleButton)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        with(channelRecyclerView) {
            adapter = channelAdapter
            layoutManager = linearLayoutManager
            isNestedScrollingEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activityViewModel.favouriteGroup.removeObservers(this)
        activityViewModel.favouriteGroupFailure.removeObservers(this)
    }

    private fun showAddToFavourite() {
        favouriteButton.visibility = View.VISIBLE
        favouriteButton.setOnClickListener {
            activityViewModel.setFavouriteGroup(passedGroup)
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