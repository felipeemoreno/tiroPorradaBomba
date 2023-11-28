package com.nativemodule.clisitefmodule

import android.annotation.SuppressLint
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

    @SuppressLint("LongLogTag")
    @ReactMethod
    fun success(result: Any?) {
        if (resultHandler != null) {
            resultHandler!!.invoke(result)
        } else {
            Log.d("com.nativemodule.clisitefmodule.SiTefClient::success", result.toString())
        }
    }

    @SuppressLint("LongLogTag")
    @ReactMethod
    fun error(errorCode: String, errorMessage: String?, errorDetails: Any? = null) {
        if (resultHandler != null) {
            resultHandler!!.invoke(errorCode, errorMessage, errorDetails);
        } else {
            Log.d("com.nativemodule.clisitefmodule.SiTefClient::error::$errorCode", errorMessage.toString())
        }
    }
}

