package com.lucianbc.receiptscan.presentation.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.presentation.Event
import com.lucianbc.receiptscan.presentation.draft.CategoryFragment
import com.lucianbc.receiptscan.presentation.draft.CurrencyFragment
import com.lucianbc.receiptscan.presentation.scanner.ScannerActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var eventBus: EventBus

    @SuppressLint("PrivateResource")
    private val inn = R.anim.mtrl_bottom_sheet_slide_in
    @SuppressLint("PrivateResource")
    private val out = R.anim.mtrl_bottom_sheet_slide_out

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
                pager.currentItem = HomePagerAdapter.RECEIPTS
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

    override fun onStart() {
        super.onStart()
        eventBus.register(this)
    }

    override fun onStop() {
        super.onStop()
        eventBus.unregister(this)
    }

    @Subscribe
    fun onCurrencyTapped(event: Event.CurrencyTapped) {
        val frag = CurrencyFragment(event.callback)
        addFragment(CurrencyFragment.TAG, frag)
    }

    @Subscribe
    fun onCategoryTapped(event: Event.CategoryTapped) {
        val frag = CategoryFragment(event.callback)
        addFragment(CategoryFragment.TAG, frag)
    }

    private fun addFragment(tag: String, frag: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(inn, out, inn, out)
            .add(R.id.homeFrame, frag, tag)
            .addToBackStack(tag)
            .commit()
    }
}
