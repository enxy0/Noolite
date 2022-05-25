package com.enxy.noolite.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.enxy.noolite.presentation.ui.theme.AppTheme

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            AppTheme {
                HomeScreen(
                    navigateToSettings = {
                        findNavController().navigate(HomeFragmentDirections.actionHomeToSettings())
                    },
                    navigateToGroup = { group ->
                        findNavController().navigate(HomeFragmentDirections.actionHomeToDetail(group))
                    },
                    navigateToScript = {
                        findNavController().navigate(HomeFragmentDirections.actionHomeToScript())
                    }
                )
            }
        }
    }
}
