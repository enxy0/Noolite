package com.enxy.noolite.features.channel

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.enxy.noolite.R
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.extension.failure
import com.enxy.noolite.core.extension.getActivityViewModel
import com.enxy.noolite.core.extension.observe
import com.enxy.noolite.core.platform.BaseFragment
import com.enxy.noolite.features.MainViewModel
import com.enxy.noolite.features.model.GroupModel
import kotlinx.android.synthetic.main.fragment_channel.*


class ChannelFragment : BaseFragment() {
    private lateinit var channelAdapter: ChannelAdapter
    private lateinit var viewModel: MainViewModel
    private val passedGroupModel: GroupModel
        get() = requireArguments().getSerializable(GROUP_MODEL_KEY) as GroupModel
    private val hasPassedData: Boolean
        get() = arguments != null

    override val layoutId = R.layout.fragment_channel

    companion object {
        fun newInstance(groupModel: GroupModel) = ChannelFragment().apply {
            arguments = Bundle().apply { putSerializable(GROUP_MODEL_KEY, groupModel) }
        }

        fun newInstance() = ChannelFragment()

        const val GROUP_MODEL_KEY = "group_model"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getActivityViewModel(this)
        setUpViews()
        if (hasPassedData) {
            setUpBackButton()
            if (isNotFavouriteFragment())
                setUpFavouriteButton()
            renderGroupElement(passedGroupModel)
        } else {
            with(viewModel) {
                observe(favouriteGroupElement, ::renderGroupElement)
                failure(favouriteFailure, ::handleFailure)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (hasPassedData) {
            setToolbarTitle(passedGroupModel.name)
            setUpBackButton()
        } else {
            if (viewModel.favouriteGroupElement.value != null)
                setToolbarTitle(viewModel.favouriteGroupElement.value!!.name)
            else
                setToolbarTitle(R.string.title_favourite)
        }
    }

    private fun handleFailure(failureNullable: Failure?) {
        failureNullable?.let {
            if (!errorLayout.isVisible) {
                channelRecyclerView.isGone = true
                errorLayout.isVisible = true
            }
            setToolbarTitle(R.string.title_favourite)
        }
    }

    private fun renderGroupElement(groupModel: GroupModel?) {
        groupModel?.let {
            setToolbarTitle(it.name)
            if (errorLayout.isVisible) {
                channelRecyclerView.isVisible = true
                errorLayout.isGone = true
            }
            channelAdapter.updateData(it.channelList)
        }
    }

    override fun onPause() {
        super.onPause()
        hideBackButton()
        if (hasPassedData)
            setToolbarTitle(R.string.title_groups)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.let {
            it.favouriteGroupElement.removeObservers(this)
            it.favouriteFailure.removeObservers(this)
        }
    }

    private fun setUpViews() {
        channelAdapter =
            ChannelAdapter(viewModel)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        with(channelRecyclerView) {
            adapter = channelAdapter
            layoutManager = linearLayoutManager
            isNestedScrollingEnabled = false
        }
    }

    private fun setUpFavouriteButton() {
        favouriteButton.visibility = View.VISIBLE
        favouriteButton.setOnClickListener {
            with(viewModel) { updateFavouriteGroupElement(passedGroupModel) }
            notify(R.string.message_room_added_as_favourite)
        }
    }

    private fun isNotFavouriteFragment() =
        with(viewModel) { favouriteGroupElement.value != passedGroupModel }
}