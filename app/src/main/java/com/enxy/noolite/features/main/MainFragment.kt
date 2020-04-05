package com.enxy.noolite.features.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.enxy.noolite.R
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.extension.failure
import com.enxy.noolite.core.extension.observe
import com.enxy.noolite.core.platform.BaseFragment
import com.enxy.noolite.features.MainViewModel
import com.enxy.noolite.features.channel.ChannelAdapter
import com.enxy.noolite.features.channel.ChannelFragment
import com.enxy.noolite.features.main.create.ActionGroupFragment
import com.enxy.noolite.features.model.Channel
import com.enxy.noolite.features.model.ChannelAction
import com.enxy.noolite.features.model.Group
import com.enxy.noolite.features.model.Script
import com.enxy.noolite.features.settings.SettingsFragment
import kotlinx.android.synthetic.main.content_feature_error.view.*
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseFragment(), GroupAdapter.GroupListener, ScriptAdapter.ScriptListener,
    ChannelAdapter.ChannelListener {
    private val groupAdapter: GroupAdapter = GroupAdapter(this)
    private val favouriteGroupAdapter: ChannelAdapter = ChannelAdapter(this)
    private val viewModel: MainViewModel by activityViewModels()
    private val scriptAdapter: ScriptAdapter = ScriptAdapter(this)
    override val layoutId = R.layout.fragment_main

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        failure(viewModel.groupListFailure, ::handleGroupListFailure)
        failure(viewModel.favouriteGroupFailure, ::handleFavouriteGroupFailure)
        failure(viewModel.scriptListFailure, ::handleScriptListFailure)
        observe(viewModel.groupList, ::renderGroupList)
        observe(viewModel.favouriteGroup, ::renderFavouriteGroup)
        observe(viewModel.scriptList, ::renderScriptList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarTitle(R.string.app_name)
        setUpRecyclerView()
        Log.d("MainFragment", "onViewCreated: called")
        addScript.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentHolder, ActionGroupFragment.newInstance())
                addToBackStack(ActionGroupFragment.TAG)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settingsItem -> parentFragmentManager.commit {
                replace(R.id.fragmentHolder, SettingsFragment.newInstance())
                addToBackStack(null)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpRecyclerView() {
        with(groupList) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = groupAdapter
            setHasFixedSize(true)
        }
        with(scriptList) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = scriptAdapter
            setHasFixedSize(true)
        }
        favouriteGroupAdapter.setToggleButtonVisibility(viewModel.hasToggleButton)
        with(favouriteGroup) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = favouriteGroupAdapter
            setHasFixedSize(true)
        }
    }

    private fun renderGroupList(data: ArrayList<Group>?) {
        if (!data.isNullOrEmpty()) {
            groupListError.isGone = true
            groupList.isVisible = true
            groupAdapter.updateData(data)
        }
    }

    private fun renderFavouriteGroup(data: Group?) {
        data?.let {
            favouriteGroupError.isGone = true
            favouriteGroup.isVisible = true
            favouriteGroupAdapter.updateData(data.channelList)
        }
    }

    private fun renderScriptList(data: ArrayList<Script>?) {
        data?.let {
            scriptAdapter.updateData(data)
        }
    }

    private fun handleGroupListFailure(failure: Failure?) {
        if (groupList.size == 0 && failure != null) {
            val text = when (failure) {
                // TODO: Add more error messages
                is Failure.DataNotFound -> R.string.error_group_list_not_found
                is Failure.WifiConnectionError -> R.string.error_group_list_not_found
                is Failure.DeserializeError -> R.string.error_deserialization
                else -> R.string.error_unexpected
            }
            showErrorLayout(groupListError, text, R.drawable.ic_question)
            groupList.isGone = true
        }
    }

    private fun showErrorLayout(view: View, text: Int, icon: Int) {
        view.errorTextView.setText(text)
        view.errorImageView.setImageResource(icon)
        view.isVisible = true
    }

    private fun handleFavouriteGroupFailure(failure: Failure?) {
        if (favouriteGroup.size == 0 && failure != null) {
            val text = when (failure) {
                is Failure.DataNotFound -> R.string.error_favourite_group_not_found
                is Failure.DeserializeError -> R.string.error_deserialization
                else -> R.string.error_unexpected
            }
            showErrorLayout(favouriteGroupError, text, R.drawable.ic_star)
            favouriteGroup.isGone = true
        }
    }

    private fun handleScriptListFailure(failure: Failure?) {
        if (scriptList.size == 0 && failure != null) {
            val text = when (failure) {
                is Failure.DataNotFound -> R.string.error_script_list_not_found
                is Failure.DeserializeError -> R.string.error_deserialization
                else -> R.string.error_unexpected
            }
            showErrorLayout(scriptError, text, R.drawable.ic_script)
            scriptList.isGone = true
        }
    }

    override fun onGroupOpen(group: Group) {
        parentFragmentManager.commit {
//            setCustomAnimations(R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_in, R.anim.zoom_out)
            replace(R.id.fragmentHolder, ChannelFragment.newInstance(group))
            addToBackStack(null)
        }
    }

    override fun onTurnOnLights(group: Group) {
        for (channel in group.channelList)
            onTurnOnLight(channel)
    }

    override fun onTurnOffLights(group: Group) {
        for (channel in group.channelList)
            onTurnOffLight(channel)
    }

    override fun onScriptExecute(script: Script) {
        Log.d("GroupFragment", "onScriptExecute: script=$script")
        viewModel.runScript(script)
    }

    override fun onScriptEdit(script: Script) {
        Log.d("GroupFragment", "onScriptEdit: script=$script")
        TODO("Add edit script function")
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