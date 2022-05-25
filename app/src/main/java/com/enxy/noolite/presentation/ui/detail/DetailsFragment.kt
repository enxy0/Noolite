package com.enxy.noolite.presentation.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.enxy.noolite.presentation.ui.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {

    private val viewModel by stateViewModel<DetailsViewModel>(
        state = { navArgs<DetailsFragmentArgs>().value.toBundle() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            AppTheme {
                DetailsScreen(
                    onBackClick = { findNavController().popBackStack() },
                    viewModel = viewModel
                )
            }
        }
    }
}
