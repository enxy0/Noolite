package com.enxy.noolite.features

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import com.enxy.noolite.R
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.extension.*
import com.enxy.noolite.core.platform.BaseFragment
import com.enxy.noolite.core.platform.FileManager
import com.enxy.noolite.features.model.GroupModel
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

    override fun onResume() {
        super.onResume()
        setToolbarTitle(R.string.title_settings)
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private fun setUpViews() {
        with(viewModel) {
            when (settingsManager.currentTheme) {
                FileManager.WHITE_BLUE_THEME_VALUE -> whiteBlueThemeSwitch.isChecked = true
                FileManager.WHITE_RED_THEME_VALUE -> whiteRedThemeSwitch.isChecked = true
                FileManager.DARK_GREEN_THEME_VALUE -> darkGreenThemeSwitch.isChecked = true
                FileManager.BLACK_BLUE_THEME_VALUE -> blackBlueThemeSwitch.isChecked = true
                else -> darkGreenThemeSwitch.isChecked = true
            }
            ipAddressEditText.setText(settingsManager.ipAddress)
            currentThemeTextView.text =
                settingsManager.currentTheme.fromUnderscoreToNormal().capitalizeWords()
            loginInputLayout.isEnabled = false
            passwordInputLayout.isEnabled = false
            toggleButtonSwitch.isChecked = settingsManager.hasToggleButton
        }
    }

    private fun handleUpdate(groupListModelNullable: ArrayList<GroupModel>?) {
        groupListModelNullable?.let { groupListModel ->
            if (groupListModel.isNotEmpty()) {
                notify(R.string.message_data_updated)
            }
            viewModel.let {
                it.groupElementList.removeObservers(this)
                it.groupFailure.removeObservers(this)
                it.groupFailure.value = null
            }
        }
    }

    private fun handleError(failureNullable: Failure?) {
        failureNullable?.let {
            notifyError(R.string.error_general)
            viewModel.let {
                it.groupElementList.removeObservers(this)
                it.groupFailure.removeObservers(this)
                it.groupElementList.value = null
            }
        }
    }

    private fun setUpUiHandlers() {
        authenticationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                loginInputLayout.isEnabled = true
                passwordInputLayout.isEnabled = true
            } else {
                loginInputLayout.isEnabled = false
                passwordInputLayout.isEnabled = false
            }
        }

        whiteBlueThemeSwitch.setOnClickListener {
            viewModel.settingsManager.currentTheme = FileManager.WHITE_BLUE_THEME_VALUE
            removeThemeOnClickListeners()
            restartAppWithAnimation()
        }

        whiteRedThemeSwitch.setOnClickListener {
            viewModel.settingsManager.currentTheme = FileManager.WHITE_RED_THEME_VALUE
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
                groupElementList.value = null
                groupFailure.value = null
                observe(groupElementList, ::handleUpdate)
                failure(groupFailure, ::handleError)
                settingsManager.ipAddress = ipAddressEditText.text.toString()
                loadGroupElementList(force = true, ipAddress = settingsManager.ipAddress)
            }
        }

        authenticationLinearLayout.setOnClickListener {
            notify(R.string.message_authentication_is_not_available)
        }

        toggleButtonSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.settingsManager.hasToggleButton = checked
        }
        toggleButtonLinearLayout.setOnClickListener {
            toggleButtonSwitch.let { it.isChecked = !it.isChecked }
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
    }

    override fun onStop() {
        super.onStop()
        viewModel.let {
            it.groupElementList.removeObservers(this)
            it.groupFailure.removeObservers(this)
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
        whiteRedThemeSwitch.setOnClickListener(null)
        whiteBlueThemeSwitch.setOnClickListener(null)
        darkGreenThemeSwitch.setOnClickListener(null)
        blackBlueThemeSwitch.setOnClickListener(null)
    }
}