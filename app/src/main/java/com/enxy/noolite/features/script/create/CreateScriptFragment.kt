package com.enxy.noolite.features.script.create

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
import com.enxy.noolite.features.model.GroupModel
import kotlinx.android.synthetic.main.fragment_create_script.*

class CreateScriptFragment : BaseFragment(), ActionGroupAdapter.ActionListener {
    override val layoutId: Int = R.layout.fragment_create_script
    private lateinit var viewModel: MainViewModel
    private lateinit var actionGroupAdapter: ActionGroupAdapter

    companion object {
        const val TAG = "CreateScriptFragment"
        fun newInstance() = CreateScriptFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showFinishScript()
        viewModel = getActivityViewModel(this)
        actionGroupAdapter = ActionGroupAdapter(this)
        with(viewModel) {
            observe(groupElementList, ::renderData)
//            TODO: Close fragment on failure and notify user?
//            failure(groupFailure, ::handleFailure)
        }
        with(createGroupList) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = actionGroupAdapter
        }

    }

    private fun renderData(groupList: ArrayList<GroupModel>?) {
        groupList?.let { actionGroupAdapter.updateData(it) }
    }

    private fun showFinishScript() = with(finishScript) {
        alpha = 0f
        scaleX = 0f
        scaleY = 0f
        animate()
            .setDuration(500L)
            .scaleX(1f).scaleY(1f).alpha(1f)
            .setInterpolator(OvershootInterpolator())
            .start()
    }


    override fun onTurnOnActionChange(isChecked: Boolean, groupModel: GroupModel) {
        Log.d(
            "CreateScriptFragment",
            "onTurnOnActionChange: isChecked=$isChecked, groupModel=$groupModel"
        )
    }

    override fun onTurnOffActionChange(isChecked: Boolean, groupModel: GroupModel) {
        Log.d(
            "CreateScriptFragment",
            "onTurnOffActionChange: isChecked=$isChecked, groupModel=$groupModel"
        )
    }

    override fun onOpenGroup(groupModel: GroupModel) {
        Log.d("CreateScriptFragment", "onOpenGroup: groupModel=$groupModel")
    }
}