package com.enxy.noolite.features.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.enxy.noolite.R
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.extension.*
import com.enxy.noolite.core.platform.BaseFragment
import com.enxy.noolite.core.platform.FileManager
import com.enxy.noolite.features.MainActivity
import com.enxy.noolite.features.MainViewModel
import com.enxy.noolite.features.model.Group
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val activityViewModel: MainViewModel by activityViewModels()
    private lateinit var viewModel: SettingsViewModel
    override val layoutId = R.layout.fragment_settings

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
        viewModel = getViewModel(this, viewModelFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSettingsValues()
        setUpUiHandlers()
    }

    private fun initSettingsValues() = with(viewModel) {
        when (currentTheme) {
            FileManager.WHITE_BLUE_THEME_VALUE -> whiteBlueThemeSwitch.isChecked = true
            FileManager.DARK_GREEN_THEME_VALUE -> darkGreenThemeSwitch.isChecked = true
            FileManager.BLACK_BLUE_THEME_VALUE -> blackBlueThemeSwitch.isChecked = true
            else -> darkGreenThemeSwitch.isChecked = true
        }
        wifiNotificationButtonSwitch.isChecked = wifiNotification
        ipAddressEditText.setText(ipAddress)
        currentThemeTextView.text = currentTheme.fromUnderscoreToNormal().capitalizeWords()
        toggleButtonSwitch.isChecked = hasToggleButton
        appVersionSummary.text = appVersion
        buildNumberSummary.text = appBuildNumber
    }

    private fun handleUpdate(groupList: ArrayList<Group>?) {
        if (!groupList.isNullOrEmpty()) {
            activityViewModel.updateGroupList(groupList)
            notify(R.string.message_data_updated)
            viewModel.let {
                it.groupList.removeObservers(this)
                it.failure.removeObservers(this)
            }
        }
    }

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
            viewModel.setLightTheme()
            removeThemeOnClickListeners()
            restartAppWithAnimation()
        }

        darkGreenThemeSwitch.setOnClickListener {
            viewModel.setDarkTheme()
            removeThemeOnClickListeners()
            restartAppWithAnimation()
        }

        blackBlueThemeSwitch.setOnClickListener {
            viewModel.setBlackTheme()
            removeThemeOnClickListeners()
            restartAppWithAnimation()
        }

        updateDataButton.setOnClickListener {
            val ipAddress: String = ipAddressEditText.text.toString()
            with(viewModel) {
                prepareToFetchGroupList(ipAddress)
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
            activityViewModel.loadTestData()
            notify(R.string.message_data_updated)
        }

        settingsScrollView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalFocusChangeListener,
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (settingsScrollView != null && settingsScrollView.height != 0)
                    if (viewModel.themeChanged) {
                        settingsScrollView.scrollTo(viewModel.scrollX, viewModel.scrollY)
                        viewModel.clearThemeChangeValues()
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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.groupList.removeObservers(this)
        viewModel.failure.removeObservers(this)
    }

    private fun restartAppWithAnimation() {
        val newMainActivity = Intent(requireContext(), MainActivity::class.java).apply {
            putExtra(FileManager.THEME_CHANGED, true)
            putExtra(FileManager.SCROLL_X_KEY, settingsScrollView.scrollX)
            putExtra(FileManager.SCROLL_Y_KEY, settingsScrollView.scrollY)
        }
        startActivity(newMainActivity)
        getMainActivity().finish()
        getMainActivity().overridePendingTransition(0, R.anim.fade_out)
    }

    private fun removeThemeOnClickListeners() {
        // To prevent app from restarting to many times, if user did more than one click
        whiteBlueThemeSwitch.setOnClickListener(null)
        darkGreenThemeSwitch.setOnClickListener(null)
        blackBlueThemeSwitch.setOnClickListener(null)
    }
}