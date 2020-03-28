package com.enxy.noolite.features.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.enxy.noolite.R
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.extension.failure
import com.enxy.noolite.core.extension.getActivityViewModel
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
import kotlinx.android.synthetic.main.content_error.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject


class MainFragment : BaseFragment(), GroupAdapter.GroupListener, ScriptAdapter.ScriptListener,
    ChannelAdapter.ChannelListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val groupAdapter: GroupAdapter = GroupAdapter(this)
    private val channelAdapter: ChannelAdapter = ChannelAdapter(this)
    private lateinit var viewModel: MainViewModel
    private val scriptAdapter: ScriptAdapter = ScriptAdapter(this)
    override val layoutId = R.layout.fragment_main

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = getActivityViewModel(this)
        observe(viewModel.groupList, ::renderData)
        observe(viewModel.favouriteGroup, ::renderFavouriteGroup)
        failure(viewModel.failure, ::handleError)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        Log.d("MainFragment", "onViewCreated: called")
        addScript.setOnClickListener {
            parentFragmentManager.commit {
                setCustomAnimations(
                    R.anim.zoom_in,
                    R.anim.zoom_out,
                    R.anim.zoom_in,
                    R.anim.zoom_out
                )
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
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()
        setToolbarTitle(R.string.app_name)
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
        channelAdapter.setToggleButtonVisibility(viewModel.hasToggleButton)
        with(channelList) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = channelAdapter
            setHasFixedSize(true)
        }
    }

    private fun renderData(data: ArrayList<Group>?) {
        if (!data.isNullOrEmpty()) {
            Log.d("MainFragment", "renderData: data=$data")
            if (errorLayout.isVisible) {
                errorLayout.isGone = true
                groupList.isVisible = true
            }
            groupAdapter.updateData(data)
        }
    }

    private fun renderFavouriteGroup(data: Group?) {
        data?.let {
            Log.d("MainFragment", "renderFavouriteGroup: data=$data")
            channelAdapter.updateData(data.channelList)
        }
    }

    private fun handleError(failure: Failure?) {
        if (groupList.size == 0) {
            groupList.isGone = true
            errorLayout.isVisible = true
            errorLayout.errorImageView.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_list)
            )
            when (failure) {
                is Failure.DataNotFound -> {
                    errorLayout.errorTextView.setText(R.string.error_group_list_not_found)
                }
                is Failure.WifiConnectionError -> {
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
            setCustomAnimations(R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_in, R.anim.zoom_out)
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