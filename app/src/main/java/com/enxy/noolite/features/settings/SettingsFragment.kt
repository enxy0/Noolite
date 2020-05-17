package com.enxy.noolite.features.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import com.enxy.noolite.R
import com.enxy.noolite.core.base.BaseFragment
import com.enxy.noolite.core.data.Group
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.utils.Constants.Companion.BLACK_THEME_VALUE
import com.enxy.noolite.core.utils.Constants.Companion.DARK_THEME_VALUE
import com.enxy.noolite.core.utils.Constants.Companion.WHITE_THEME_VALUE
import com.enxy.noolite.core.utils.extension.capitalizeWords
import com.enxy.noolite.core.utils.extension.failure
import com.enxy.noolite.core.utils.extension.fromUnderscoreToSpaces
import com.enxy.noolite.core.utils.extension.observe
import com.enxy.noolite.features.MainActivity
import com.enxy.noolite.features.MainViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsFragment : BaseFragment() {
    private val activityViewModel: MainViewModel by sharedViewModel()
    private val viewModel: SettingsViewModel by inject()
    override val layoutId: Int
        get() = R.layout.fragment_settings

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarTitle(R.string.title_settings)
        initSettingsValues()
        setUpUiHandlers()
    }

    /**
     * Applies saved values (settings) to the UI.
     */
    private fun initSettingsValues() = with(viewModel) {
        when (currentTheme) {
            BLACK_THEME_VALUE -> blackBlueThemeSwitch.isChecked = true
            WHITE_THEME_VALUE -> whiteBlueThemeSwitch.isChecked = true
            else -> darkGreenThemeSwitch.isChecked = true
        }
        wifiNotificationButtonSwitch.isChecked = wifiNotification
        ipAddressEditText.setText(ipAddress)
        currentThemeTextView.text = currentTheme.fromUnderscoreToSpaces().capitalizeWords()
        toggleButtonSwitch.isChecked = hasToggleButton
        appVersionSummary.text = appVersion
        buildNumberSummary.text = appBuildNumber
    }

    /**
     * Handles success fetch of GroupList from the given IP Address.
     */
    private fun handleUpdate(groupList: ArrayList<Group>?) {
        if (!groupList.isNullOrEmpty()) {
            activityViewModel.updateGroupList(groupList) // updating shared list
            notify(R.string.message_data_updated)
            viewModel.let {
                it.groupList.removeObservers(this)
                it.failure.removeObservers(this)
            }
        }
    }

    /**
     * Handles failure from fetching GroupList.
     * TODO: Add more messages to notify the user about failure.
     */
    private fun handleError(failure: Failure?) {
        failure?.let {
            notifyError(R.string.error_general)
            viewModel.let {
                it.groupList.removeObservers(this)
                it.failure.removeObservers(this)
            }
        }
    }

    private fun setUpUiHandlers() {
        whiteBlueThemeSwitch.setOnClickListener {
            changeTheme(WHITE_THEME_VALUE)
        }

        darkGreenThemeSwitch.setOnClickListener {
            changeTheme(DARK_THEME_VALUE)
        }

        blackBlueThemeSwitch.setOnClickListener {
            changeTheme(BLACK_THEME_VALUE)
        }

        updateDataButton.setOnClickListener {
            val ipAddress: String = ipAddressEditText.text.toString()
            with(viewModel) {
                prepareToFetchGroupList(ipAddress) // clear all the previous attempts
                observe(groupList, ::handleUpdate)
                failure(failure, ::handleError)
                fetchGroupList(ipAddress)
            }
        }

        toggleButtonSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.setHasToggleButton(checked)
        }

        toggleButtonLinearLayout.setOnClickListener {
            toggleButtonSwitch.let { it.isChecked = !it.isChecked }
        }

        wifiNotificationButtonSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.setWifiNotification(checked)
        }

        wifiNotificationLinearLayout.setOnClickListener {
            wifiNotificationButtonSwitch.let { it.isChecked = !it.isChecked }
        }

        loadTestDataLinearLayout.setOnClickListener {
            activityViewModel.setTestData()
            notify(R.string.message_data_updated)
        }

        settingsScrollView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalFocusChangeListener,
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (settingsScrollView != null && settingsScrollView.height != 0)
                    if (viewModel.themeChanged) {
                        settingsScrollView.scrollTo(
                            viewModel.scrollX,
                            viewModel.scrollY
                        ) // restore scroll after theme change
                        viewModel.clearThemeChangeValues() // finish theme change process and remove unnecessary data
                    }
                settingsScrollView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

            override fun onGlobalFocusChanged(oldFocus: View?, newFocus: View?) = Unit
        })

        githubLayout.setOnClickListener {
            val githubUri = Uri.parse(getString(R.string.settings_github_summary))
            val openGithub = Intent(Intent.ACTION_VIEW, githubUri)
            startActivity(openGithub)
        }

        authorLayout.setOnClickListener {
            val vkUri = Uri.parse(getString(R.string.settings_vk_url))
            val openVk = Intent(Intent.ACTION_VIEW, vkUri)
            startActivity(openVk)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.groupList.removeObservers(this)
        viewModel.failure.removeObservers(this)
    }

    /**
     * Starts the process of seamless theme change which opens [MainActivity] with new theme.
     */
    private fun changeTheme(themeName: String) {
        viewModel.setTheme(themeName)

        // Remove listeners to prevent app from restarting too many times
        // if the user clicked more than one time
        whiteBlueThemeSwitch.setOnClickListener(null)
        darkGreenThemeSwitch.setOnClickListener(null)
        blackBlueThemeSwitch.setOnClickListener(null)

        // Saving scrollView position to make theme change seamless
        val scrollX = settingsScrollView.scrollX
        val scrollY = settingsScrollView.scrollY

        startActivity(MainActivity.newThemedActivity(requireContext(), themeName, scrollX, scrollY))
        getMainActivity().finish()
        getMainActivity().overridePendingTransition(0, R.anim.fade_out)
    }
}