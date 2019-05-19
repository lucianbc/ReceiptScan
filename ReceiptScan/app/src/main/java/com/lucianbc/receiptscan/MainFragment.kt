package com.lucianbc.receiptscan


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initButtons()
    }


    private fun initAdapter() {
        fragmentManager?.apply {
            HomePagerAdapter(this).also {
                pager.adapter = it
                pager.currentItem = HomePagerAdapter.DRAFTS
            }
        }
    }

    private fun initButtons() {
        drafts_btn.setOnClickListener { setView(HomePagerAdapter.DRAFTS) }
        export_btn.setOnClickListener { setView(HomePagerAdapter.EXPORT) }
        receipts_btn.setOnClickListener { setView(HomePagerAdapter.RECEIPTS) }
        settings_btn.setOnClickListener { setView(HomePagerAdapter.SETTINGS) }
    }

    private fun setView(pos: Int) {
        pager.setCurrentItem(pos, false)
    }
}
