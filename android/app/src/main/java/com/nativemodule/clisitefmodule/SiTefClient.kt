package com.nativemodule.clisitefmodule

import android.util.Log
import br.com.softwareexpress.sitef.android.CliSiTef
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Callback

open class SiTefClient( protected val cliSiTef: CliSiTef)  {
    private var resultHandler: Callback? = null

    @ReactMethod
    fun setResultHandler(callback: Callback) {
        resultHandler = callback
    }

    @ReactMethod
    fun success(result: Any?) {
        if (resultHandler != null) {
            resultHandler!!.invoke(result)
        } else {
            Log.v("SiTefClient::success", result.toString())
        }
    }
}

