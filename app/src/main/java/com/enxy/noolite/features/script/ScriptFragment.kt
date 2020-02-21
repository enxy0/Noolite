package com.enxy.noolite.features.script

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.enxy.noolite.R
import com.enxy.noolite.core.extension.getActivityViewModel
import com.enxy.noolite.core.platform.BaseFragment
import com.enxy.noolite.features.MainViewModel
import com.enxy.noolite.features.model.Script
import kotlinx.android.synthetic.main.fragment_script.*

class ScriptFragment : BaseFragment(), ScriptAdapter.ScriptListener {
    override val layoutId = R.layout.fragment_script
    private lateinit var scriptAdapter: ScriptAdapter
    private lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = ScriptFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getActivityViewModel(this)
        scriptAdapter = ScriptAdapter(this)
        with(scriptRecyclerView) {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )
            adapter = scriptAdapter
            setHasFixedSize(true)
        }
    }

    override fun onScriptExecute(script: Script) {
        Log.d("ScriptFragment", "onScriptExecute: script=$script")
    }

    override fun onScriptEdit(script: Script) {
        Log.d("ScriptFragment", "onScriptEdit: script=$script")
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle(R.string.title_scripts)
    }
}