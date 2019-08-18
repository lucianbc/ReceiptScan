package com.lucianbc.receiptscan.presentation.home.exports.form

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lucianbc.receiptscan.R

class FormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
    }

    companion object {
        fun navIntent(context: Context) = Intent(context, FormActivity::class.java)
    }
}
