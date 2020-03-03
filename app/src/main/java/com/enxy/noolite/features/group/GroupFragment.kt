package com.enxy.noolite.features.group

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.enxy.noolite.R
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.extension.failure
import com.enxy.noolite.core.extension.getViewModel
import com.enxy.noolite.core.extension.observe
import com.enxy.noolite.core.platform.BaseFragment
import com.enxy.noolite.features.channel.ChannelFragment
import com.enxy.noolite.features.model.Group
import kotlinx.android.synthetic.main.content_error.view.*
import kotlinx.android.synthetic.main.fragment_group.*
import javax.inject.Inject


class GroupFragment : BaseFragment(), GroupAdapter.GroupListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var groupAdapter: GroupAdapter
    private lateinit var viewModel: GroupViewModel
    override val layoutId = R.layout.fragment_group

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appComponent.inject(this)
        viewModel = getViewModel(this, viewModelFactory)
        setUpViews()
        with(viewModel) {
            observe(groupList, ::renderData)
            failure(failure, ::handleError)
        }
    }

    companion object {
        fun newInstance() = GroupFragment()
    }

    private fun setUpViews() {
        groupAdapter = GroupAdapter(this)
        groupRecyclerView.adapter = groupAdapter
        val spanCount = if (isInLandscapeOrientation()) 3 else 2
        groupRecyclerView.layoutManager = GridLayoutManager(context, spanCount)
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle(R.string.title_groups)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.let {
            it.groupList.removeObservers(this)
            it.failure.removeObservers(this)
        }
    }

    private fun renderData(groupList: ArrayList<Group>?) {
        if (!groupList.isNullOrEmpty()) {
            if (errorLayout.isVisible) {
                errorLayout.isGone = true
                groupRecyclerView.isVisible = true
            }
            groupAdapter.updateData(groupList)
        }
    }

    private fun handleError(failure: Failure?) {
        if (groupRecyclerView.size == 0) {
            groupRecyclerView.isGone = true
            errorLayout.isVisible = true
            errorLayout.errorImageView.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_list)
            )
            when (failure) {
                is Failure.DataNotFound -> {
                    errorLayout.errorTextView.setText(R.string.error_group_list_not_found)
                }
                is Failure.DeserializeError -> {
                    errorLayout.errorTextView.setText(R.string.error_deserialization)
                }
            }
        }
    }

    override fun onGroupOpen(group: Group) {
        parentFragmentManager.commit {
            addToBackStack(null)
            setCustomAnimations(R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_in, R.anim.zoom_out)
            replace(R.id.fragmentHolder, ChannelFragment.newInstance(group))
        }
    }

    override fun onTurnOnLights(group: Group) {
        for (channel in group.channelList)
            viewModel.turnOnLight(channel.id)
    }

    override fun onTurnOffLights(group: Group) {
        for (channel in group.channelList)
            viewModel.turnOffLight(channel.id)
    }
}