package com.enxy.noolite.features

import android.annotation.SuppressLint
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
import com.enxy.noolite.features.adapter.ChannelAdapter
import com.enxy.noolite.features.model.GroupModel
import kotlinx.android.synthetic.main.fragment_channel.*


class ChannelFragment : BaseFragment() {
    private lateinit var channelAdapter: ChannelAdapter
    private lateinit var viewModel: MainViewModel
    override val layoutId = R.layout.fragment_channel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getActivityViewModel(this)
        setUpViews()
        if (isOpenedAsFavouriteFragment()) {
            viewModel.let {
                observe(it.favouriteGroupElement, ::renderGroupElement)
                failure(it.favouriteFailure, ::handleFailure)
            }
        } else {
            viewModel.let {
                observe(it.chosenGroupElement, ::renderGroupElement)
                failure(it.chosenFailure, ::handleFailure)
            }
            setUpBackButton()
            if (isNotFavouriteFragment())
                setUpFavouriteButton()
        }
    }

    private fun handleFailure(failure: Failure?) {
        failure?.let {
            if (!errorLayout.isVisible) {
                channelRecyclerView.isVisible = false
                errorLayout.isVisible = true
            }
            setToolbarTitle(R.string.title_my_room)
        }
    }

    private fun renderGroupElement(groupModel: GroupModel?) {
        groupModel?.let {
            setToolbarTitle(groupModel.name)
            if (isOpenedAsFavouriteFragment())
                viewModel.favouriteFailure.value = null
            else
                viewModel.chosenFailure.value = null
            if (errorLayout.isVisible)
                errorLayout.isGone = true
            with(channelAdapter) {
                clear()
                addAll(groupModel)
                notifyDataSetChanged()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        hideBackButton()
        if (isOpenedAsFavouriteFragment())
            setToolbarTitle(R.string.title_rooms)
        viewModel.let {
            if (it.chosenGroupElement.value != null)
                it.chosenGroupElement.value = null
            it.chosenGroupElement.removeObservers(this)
            it.chosenFailure.removeObservers(this)
            it.favouriteGroupElement.removeObservers(this)
            it.favouriteFailure.removeObservers(this)
        }
    }

    @SuppressLint("WrongConstant")
    private fun setUpViews() {
        channelAdapter = ChannelAdapter(viewModel)
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
            with(viewModel) { updateFavouriteGroupElement(chosenGroupElement.value!!) }
            notify(R.string.message_room_added_as_favourite)
        }
    }

    private fun isNotFavouriteFragment() = with(viewModel) { favouriteGroupElement.value != chosenGroupElement.value }

    private fun isOpenedAsFavouriteFragment() = viewModel.chosenGroupElement.value == null
}