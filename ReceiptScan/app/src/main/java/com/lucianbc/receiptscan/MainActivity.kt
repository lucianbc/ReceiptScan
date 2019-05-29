package com.lucianbc.receiptscan

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAdapter()
        initButtons()
    }

    private fun initAdapter() {
        supportFragmentManager.apply {
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
        scanner_button.setOnClickListener { goToScanner() }
    }

    private fun setView(pos: Int) = pager.setCurrentItem(pos, false)

    private fun goToScanner() {
        val intent = ScannerActivity.navIntent(this)
        startActivity(intent)
    }
}
