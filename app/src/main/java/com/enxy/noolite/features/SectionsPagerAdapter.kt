package com.enxy.noolite.features

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.enxy.noolite.core.platform.BaseFragment
import com.enxy.noolite.features.channel.ChannelFragment
import com.enxy.noolite.features.group.GroupFragment
import javax.inject.Inject


class SectionsPagerAdapter @Inject constructor(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val listOfFragments = arrayListOf(
        ChannelFragment.newInstance(),
        GroupFragment.newInstance()
    )

    override fun getItem(position: Int): BaseFragment {
        // getItem is called to instantiate the fragment for the given page.
        return listOfFragments[position]
    }

    override fun getCount(): Int = 2
}