package com.nativemodule.clisitefmodule

import android.util.Log
import br.com.softwareexpress.sitef.android.CliSiTef
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

open class SiTefClient( protected val cliSiTef: CliSiTef)  {
    private var resultHandler: com.facebook.react.bridge.Callback? = null

    @ReactMethod
    fun setResultHandler(callback: com.facebook.react.bridge.Callback) {
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

