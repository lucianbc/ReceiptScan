package com.lucianbc.receiptscan.presentation.home

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.lucianbc.receiptscan.presentation.home.drafts.DraftsFragment
import com.lucianbc.receiptscan.presentation.home.exports.ExportFragment
import com.lucianbc.receiptscan.presentation.home.receipts.ReceiptsFragment
import com.lucianbc.receiptscan.presentation.home.settings.SettingsFragment

@SuppressLint("WrongConstant")
class HomePagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentList = listOf(
        ExportFragment(),
        DraftsFragment(),
        ReceiptsFragment(),
        SettingsFragment()
    )

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

    enum class Page(val position: Int) {
        EXPORT(0),
        DRAFTS(1),
        RECEIPTS(2),
        SETTINGS(3)
    }

    companion object {
        val EXPORT = Page.EXPORT
        val DRAFTS = Page.DRAFTS
        val RECEIPTS = Page.RECEIPTS
        val SETTINGS = Page.SETTINGS
    }
}