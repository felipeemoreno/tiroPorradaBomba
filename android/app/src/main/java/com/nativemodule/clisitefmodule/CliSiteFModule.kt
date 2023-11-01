package com.nativemodule

import android.util.Log
import br.com.softwareexpress.sitef.android.CliSiTef
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod


class CliSiteFModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    // add to CliSiteF.kt
    override fun getName() = "CliSiteFModule"

    init {
        val cliSiTef = CliSiTef(reactContext)
//        println(cliSiTef.version)
    }

    @ReactMethod
    fun ping(text: String) {
        Log.d("CliSiteFModule", "Ping: $text")
    }
}



