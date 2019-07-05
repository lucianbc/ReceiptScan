package com.lucianbc.receiptscan.presentation.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.lucianbc.receiptscan.presentation.home.drafts.DraftsFragment
import com.lucianbc.receiptscan.presentation.home.receipts.ReceiptsFragment

class HomePagerAdapter(fm: FragmentManager)
    : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentList = listOf(
        ExportFragment(),
        DraftsFragment(),
        ReceiptsFragment(),
        SettingsFragment()
    )

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

    companion object {
        const val EXPORT = 0
        const val DRAFTS = 1
        const val RECEIPTS = 2
        const val SETTINGS = 3
    }
}