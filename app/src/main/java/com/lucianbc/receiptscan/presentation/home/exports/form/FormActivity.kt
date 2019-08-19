package com.lucianbc.receiptscan.presentation.home.exports.form

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_form_container.*

class FormActivity
    : BaseActivity<FormViewModel>(FormViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        closeFormBtn.setOnClickListener { this.finish() }
    }

    companion object {
        fun navIntent(context: Context) = Intent(context, FormActivity::class.java)
    }
}
