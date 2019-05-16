package com.lucianbc.receiptscan.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseActivity
import com.lucianbc.receiptscan.databinding.ActivityMainBinding
import com.lucianbc.receiptscan.view.fragment.homepage.Drafts
import com.lucianbc.receiptscan.view.fragment.homepage.Receipts
import com.lucianbc.receiptscan.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity:
    BaseActivity<MainViewModel>(MainViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scan_btn.setOnClickListener(goToScanner)
        setupBinding()
        observe(viewModel)
    }

    private fun observe(vm: MainViewModel) {
        vm.state.observe(this, Observer {
            when (it) {
                MainViewModel.State.Drafts -> replaceFragment(Drafts())
                MainViewModel.State.Receipts -> replaceFragment(Receipts())
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

    private fun setupBinding() {
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private val goToScanner = View.OnClickListener {
        startActivity(Intent(this, ScannerActivity::class.java))
    }
}
