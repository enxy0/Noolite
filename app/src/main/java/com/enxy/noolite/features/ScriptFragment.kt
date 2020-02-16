package com.enxy.noolite.features

import com.enxy.noolite.R
import com.enxy.noolite.core.platform.BaseFragment

class ScriptFragment : BaseFragment() {
    override val layoutId = R.layout.fragment_script

    companion object {
        fun newInstance() = ScriptFragment()
    }

}