package com.enxy.noolite.features.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.enxy.noolite.R
import com.enxy.noolite.core.base.BaseFragment
import com.enxy.noolite.core.data.Channel
import com.enxy.noolite.core.data.ChannelAction
import com.enxy.noolite.core.data.Group
import com.enxy.noolite.core.data.Script
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.utils.extension.failure
import com.enxy.noolite.core.utils.extension.observe
import com.enxy.noolite.features.MainViewModel
import com.enxy.noolite.features.channel.ChannelAdapter
import com.enxy.noolite.features.channel.ChannelFragment
import com.enxy.noolite.features.main.create.ActionGroupFragment
import com.enxy.noolite.features.settings.SettingsFragment
import kotlinx.android.synthetic.main.content_feature_error.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class MainFragment : BaseFragment(), GroupAdapter.GroupListener, ScriptAdapter.ScriptListener,
    ChannelAdapter.ChannelListener {
    private val viewModel: MainViewModel by sharedViewModel()
    private val groupAdapter = GroupAdapter(this)
    private val scriptAdapter = ScriptAdapter(this)
    private val favouriteGroupAdapter = ChannelAdapter(this)
    override val layoutId: Int
        get() = R.layout.fragment_main

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(viewModel) {
            failure(groupListFailure, ::handleGroupListFailure)
            failure(favouriteGroupFailure, ::handleFavouriteGroupFailure)
            failure(scriptListFailure, ::handleScriptListFailure)
            observe(groupList, ::renderGroupList)
            observe(favouriteGroup, ::renderFavouriteGroup)
            observe(scriptList, ::renderScriptList)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setToolbarTitle(R.string.app_name)
        setUpRecyclerView()
        addScript.setOnClickListener {
            // TODO: show notification if group list is empty
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentHolder, ActionGroupFragment.newInstance())
                .addToBackStack(ActionGroupFragment.TAG)
                .commit()
        }
        removeFavourite.setOnClickListener {
            favouriteGroupAdapter.clear()
            viewModel.clearFavouriteGroup()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settingsItem -> parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentHolder, SettingsFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpRecyclerView() {
        groupList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = groupAdapter
            setHasFixedSize(true)
        }
        scriptList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = scriptAdapter
            setHasFixedSize(true)
        }
        favouriteGroupAdapter.setToggleButtonVisibility(viewModel.hasToggleButton)
        favouriteGroup.apply {
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
        if (data != null) {
            if (data.isNotEmpty()) {
                scriptError.isGone = true
                scriptList.isVisible = true
                scriptAdapter.updateData(data)
            } else {
                onEmptyScriptList()
            }
        }
    }

    private fun handleGroupListFailure(failure: Failure?) {
        if (groupAdapter.itemCount == 0 && failure != null) {
            val text = when (failure) {
                // TODO: Add more error messages
                is Failure.DataNotFound -> R.string.error_group_list_not_found
                is Failure.ServerError -> R.string.error_group_list_not_found
                is Failure.WifiConnectionError -> R.string.error_group_list_not_found
                is Failure.DeserializeError -> R.string.error_deserialization
            }
            showErrorLayout(groupListError, text, R.drawable.ic_question)
            groupList.isGone = true
        }
    }

    private fun handleFavouriteGroupFailure(failure: Failure?) {
        if (failure != null) {
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
        if (scriptAdapter.itemCount == 0 && failure != null) {
            val text = when (failure) {
                is Failure.DataNotFound -> R.string.error_script_list_not_found
                is Failure.DeserializeError -> R.string.error_deserialization
                else -> R.string.error_unexpected
            }
            showErrorLayout(scriptError, text, R.drawable.ic_script)
            scriptList.isGone = true
        }
    }

    private fun showErrorLayout(view: View, text: Int, icon: Int) {
        view.errorTextView.setText(text)
        view.errorImageView.setImageResource(icon)
        view.isVisible = true
    }

    override fun onGroupOpen(group: Group) {
        parentFragmentManager.beginTransaction()
//            setCustomAnimations(R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_in, R.anim.zoom_out)
            .replace(R.id.fragmentHolder, ChannelFragment.newInstance(group))
            .addToBackStack(null)
            .commit()
    }

    override fun onTurnOnLights(group: Group) {
        for (channel in group.channelList)
            onTurnOnLight(channel)
    }

    override fun onTurnOffLights(group: Group) {
        for (channel in group.channelList)
            onTurnOffLight(channel)
    }

    override fun onEmptyScriptList() {
        handleScriptListFailure(Failure.DataNotFound)
    }

    override fun onScriptExecute(script: Script) {
        viewModel.runScript(script)
    }

    override fun onScriptRemove(script: Script) {
        viewModel.removeScript(script)
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