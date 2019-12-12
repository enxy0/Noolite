package com.enxy.noolite.features

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
            with(viewModel) {
                observe(favouriteGroupElement, ::renderGroupElement)
                failure(favouriteFailure, ::handleFailure)
            }
        } else {
            with(viewModel) {
                observe(chosenGroupElement, ::renderGroupElement)
                failure(chosenFailure, ::handleFailure)
            }
            setUpBackButton()
            if (isNotFavouriteFragment())
                setUpFavouriteButton()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isOpenedAsFavouriteFragment()) {
            if (viewModel.favouriteGroupElement.value == null)
                setToolbarTitle(R.string.title_favourite)
            else
                setToolbarTitle(viewModel.favouriteGroupElement.value!!.name)
        } else {
            setUpBackButton()
            setToolbarTitle(viewModel.chosenGroupElement.value!!.name)
        }
    }

    companion object {
        fun newInstance() = ChannelFragment()
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
            if (isOpenedAsFavouriteFragment())
                viewModel.favouriteFailure.value = null
            else
                viewModel.chosenFailure.value = null
            if (errorLayout.isVisible) {
                channelRecyclerView.isVisible = true
                errorLayout.isGone = true
            }
            with(channelAdapter) {
                clear()
                addAll(it)
                notifyDataSetChanged()
            }
        }
    }


    override fun onPause() {
        super.onPause()
        hideBackButton()
        if (!isOpenedAsFavouriteFragment())
            setToolbarTitle(R.string.title_groups)
        viewModel.let {
            it.chosenGroupElement.removeObservers(this)
            it.chosenFailure.removeObservers(this)
            it.favouriteGroupElement.removeObservers(this)
            it.favouriteFailure.removeObservers(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        with(viewModel.chosenGroupElement) {
            if (value != null)
                value = null
        }
    }

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

    private fun isNotFavouriteFragment() =
        with(viewModel) { favouriteGroupElement.value != chosenGroupElement.value }

    private fun isOpenedAsFavouriteFragment() = viewModel.chosenGroupElement.value == null
}