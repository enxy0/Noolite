package com.enxy.noolite.features.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import com.enxy.noolite.BuildConfig
import com.enxy.noolite.R
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.extension.*
import com.enxy.noolite.core.platform.BaseFragment
import com.enxy.noolite.core.platform.FileManager
import com.enxy.noolite.features.MainActivity
import com.enxy.noolite.features.MainViewModel
import com.enxy.noolite.features.model.Group
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment() {
    private lateinit var viewModel: MainViewModel
    override val layoutId = R.layout.fragment_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getActivityViewModel(this)
        setUpViews()
        setUpUiHandlers()
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }

    @SuppressLint("SetTextI18n")
    private fun setUpViews() {
        with(viewModel) {
            when (settingsManager.currentTheme) {
                FileManager.WHITE_BLUE_THEME_VALUE -> whiteBlueThemeSwitch.isChecked = true
                FileManager.DARK_GREEN_THEME_VALUE -> darkGreenThemeSwitch.isChecked = true
                FileManager.BLACK_BLUE_THEME_VALUE -> blackBlueThemeSwitch.isChecked = true
                else -> darkGreenThemeSwitch.isChecked = true
            }
            wifiNotificationButtonSwitch.isChecked = settingsManager.wifiNotification
            ipAddressEditText.setText(settingsManager.ipAddress)
            currentThemeTextView.text =
                settingsManager.currentTheme.fromUnderscoreToNormal().capitalizeWords()
            toggleButtonSwitch.isChecked = settingsManager.hasToggleButton
        }
        appVersionSummary.text = "v${BuildConfig.VERSION_NAME}"
        buildNumberSummary.text = BuildConfig.VERSION_CODE.toString()
    }

    private fun handleUpdate(groupList: ArrayList<Group>?) {
        groupList?.let { groupListModel ->
            if (groupListModel.isNotEmpty()) {
                notify(R.string.message_data_updated)
            }
            viewModel.let {
                it.groupList.removeObservers(this)
                it.favouriteGroupFailure.removeObservers(this)
                it.favouriteGroupFailure.value = null
            }
        }
    }

    private fun handleError(failure: Failure?) {
        failure?.let {
            notifyError(R.string.error_general)
            viewModel.let {
                it.groupList.removeObservers(this)
                it.favouriteGroupFailure.removeObservers(this)
                it.groupList.value = null
            }
        }
    }

    private fun setUpUiHandlers() {
        whiteBlueThemeSwitch.setOnClickListener {
            viewModel.settingsManager.currentTheme = FileManager.WHITE_BLUE_THEME_VALUE
            removeThemeOnClickListeners()
            restartAppWithAnimation()
        }

        darkGreenThemeSwitch.setOnClickListener {
            viewModel.settingsManager.currentTheme = FileManager.DARK_GREEN_THEME_VALUE
            removeThemeOnClickListeners()
            restartAppWithAnimation()
        }

        blackBlueThemeSwitch.setOnClickListener {
            viewModel.settingsManager.currentTheme = FileManager.BLACK_BLUE_THEME_VALUE
            removeThemeOnClickListeners()
            restartAppWithAnimation()
        }

        updateDataButton.setOnClickListener {
            with(viewModel) {
                observe(groupList, ::handleUpdate)
                failure(groupListFailure, ::handleError)
                settingsManager.ipAddress = ipAddressEditText.text.toString()
                fetchGroupList(ipAddress = settingsManager.ipAddress, isForceUpdating = true)
            }
        }

        toggleButtonSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.settingsManager.hasToggleButton = checked
        }
        toggleButtonLinearLayout.setOnClickListener {
            toggleButtonSwitch.let { it.isChecked = !it.isChecked }
        }

        wifiNotificationButtonSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.settingsManager.wifiNotification = checked
        }

        wifiNotificationLinearLayout.setOnClickListener {
            wifiNotificationButtonSwitch.let { it.isChecked = !it.isChecked }
        }

        loadTestDataLinearLayout.setOnClickListener {
            with(viewModel) {
                groupList.value = null
                favouriteGroupFailure.value = null
                observe(groupList, ::handleUpdate)
                failure(favouriteGroupFailure, ::handleError)
                loadTestData()
            }
        }

        settingsScrollView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalFocusChangeListener,
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                settingsScrollView?.let {
                    if (settingsScrollView.height != 0) {
                        with(viewModel.settingsManager) {
                            if (themeChanged) {
                                settingsScrollView.scrollTo(scrollX, scrollY)
                                removeScrollPosition()
                            }
                        }
                        settingsScrollView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            }

            override fun onGlobalFocusChanged(oldFocus: View?, newFocus: View?) {
            }
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

    override fun onStop() {
        super.onStop()
        viewModel.let {
            it.groupList.removeObservers(this)
            it.favouriteGroupFailure.removeObservers(this)
        }
    }

    private fun restartAppWithAnimation() {
        val intent = Intent(context, MainActivity::class.java)
        saveScrollPosition(intent)
        startActivity(intent)
        getMainActivity().finish()
        getMainActivity().overridePendingTransition(
            0,
            R.anim.fade_out
        )
    }

    private fun removeScrollPosition() {
        with(viewModel) {
            settingsManager.themeChanged = false
            settingsManager.scrollX = 0
            settingsManager.scrollX = 0
        }
    }

    private fun saveScrollPosition(intent: Intent) {
        intent.putExtra(FileManager.THEME_CHANGED, true)
        val x = settingsScrollView.scrollX
        val y = settingsScrollView.scrollY
        intent.putExtra(FileManager.SCROLL_X_KEY, x)
        intent.putExtra(FileManager.SCROLL_Y_KEY, y)
    }

    private fun removeThemeOnClickListeners() {
        // To prevent app from restarting to many times, if user did more than one click
        whiteBlueThemeSwitch.setOnClickListener(null)
        darkGreenThemeSwitch.setOnClickListener(null)
        blackBlueThemeSwitch.setOnClickListener(null)
    }
}