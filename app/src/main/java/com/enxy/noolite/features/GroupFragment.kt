package com.enxy.noolite.features

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.recyclerview.widget.GridLayoutManager
import com.enxy.noolite.R
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.extension.failure
import com.enxy.noolite.core.extension.getActivityViewModel
import com.enxy.noolite.core.extension.observe
import com.enxy.noolite.core.platform.BaseFragment
import com.enxy.noolite.features.adapter.GroupAdapter
import com.enxy.noolite.features.model.GroupModel
import kotlinx.android.synthetic.main.content_error.view.*
import kotlinx.android.synthetic.main.fragment_group.*


class GroupFragment : BaseFragment() {
    private lateinit var groupAdapter: GroupAdapter
    private lateinit var viewModel: MainViewModel
    override val layoutId = R.layout.fragment_group

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getActivityViewModel(this)
        setUpViews()
        with(viewModel) {
            observe(groupElementList, ::renderData)
            failure(groupFailure, ::handleError)
        }
    }

    companion object {
        fun newInstance() = GroupFragment()
    }

    private fun setUpViews() {
        groupAdapter = GroupAdapter(this, viewModel)
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
            it.groupElementList.removeObservers(this)
            it.groupFailure.removeObservers(this)
        }
    }

    private fun renderData(groupListModelNullable: ArrayList<GroupModel>?) {
        groupListModelNullable?.let {
            if (it.isNotEmpty()) {
                if (errorLayout.isVisible) {
                    errorLayout.isGone = true
                    groupRecyclerView.isVisible = true
                }
                with(groupAdapter) {
                    clear()
                    addAll(it)
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun handleError(failureNullable: Failure?) {
        failureNullable?.let {
            if (groupRecyclerView.size == 0) {
                groupRecyclerView.isGone = true
                errorLayout.isVisible = true
                errorLayout.errorTextView.setText(R.string.error_group_list_not_found)
                errorLayout.errorImageView.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_list)
                )
            }
        }
    }
}