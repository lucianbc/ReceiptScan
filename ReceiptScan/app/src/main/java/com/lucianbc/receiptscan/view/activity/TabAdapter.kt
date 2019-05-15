package com.lucianbc.receiptscan.view.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TabAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mFragments = ArrayList<Fragment>()
    private val mTitles = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    fun add(frag: Fragment, title: String) {
        mFragments.add(frag)
        mTitles.add(title)
    }
}