package com.enxy.noolite.features

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
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

    @SuppressLint("WrongConstant")
    private fun setUpViews() {
        setToolbarTitle(R.string.title_rooms)
        groupAdapter = GroupAdapter(this, viewModel)
        groupRecyclerView.adapter = groupAdapter
        val spanCount = if (isInLandscapeOrientation()) 3 else 2
        groupRecyclerView.layoutManager = GridLayoutManager(context, spanCount)
    }

    override fun onStop() {
        super.onStop()
        viewModel.let {
            it.groupElementList.removeObservers(this)
            it.groupFailure.removeObservers(this)
        }
    }

    private fun renderData(data: ArrayList<GroupModel>?) {
        data?.let {
            if (data.isNotEmpty()) {
                groupAdapter.clear()
                groupAdapter.addAll(data)
                groupAdapter.notifyDataSetChanged()
                Log.d("GroupFragment", "renderData: called")
            }
        }
    }

    private fun handleError(failure: Failure?) {
        failure?.let {
            with(errorLayout) {
                visibility = View.VISIBLE
                errorImageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_list
                    )
                )
                errorTextView.setText(R.string.error_group_list_not_found)
                Log.d("GroupFragment", "handleError: failure=${failure.javaClass}")
                // handle different types of errors
//                when (failure) {
//                    is Failure.DataNotFound -> {}
//                    is Failure.ServerError -> {}
//                    is Failure.BinParseError -> {}
//                }
            }
        }
    }
}