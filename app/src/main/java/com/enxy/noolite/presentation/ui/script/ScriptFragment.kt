package com.enxy.noolite.presentation.ui.script

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.enxy.noolite.presentation.ui.theme.AppTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScriptFragment : Fragment() {

    private val viewModel by viewModel<ScriptViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            AppTheme {
                ScriptScreen(
                    onBackClick = {
                        findNavController().popBackStack()
                    },
                    viewModel = viewModel
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    private fun setObservers() {
        viewModel.isScriptCreated
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { isScriptCreated ->
                if (isScriptCreated) {
                    findNavController().popBackStack()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}
