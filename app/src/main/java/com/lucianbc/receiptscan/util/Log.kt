package com.lucianbc.receiptscan.util

import android.util.Log

@Suppress("unused")
inline fun <reified T> T.logd(message: String) = Log.d(T::class.java.simpleName, message)

@Suppress("unused")
inline fun <reified T> T.loge(message: String) = Log.e(T::class.java.simpleName, message)

@Suppress("unused")
inline fun <reified T> T.loge(message: String, t: Throwable) = Log.e(T::class.java.simpleName, message, t)

@Suppress("unused")
inline fun <reified T> T.logi(message: String) = Log.i(T::class.simpleName, message)