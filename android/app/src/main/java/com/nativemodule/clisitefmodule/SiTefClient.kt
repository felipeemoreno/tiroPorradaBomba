package com.nativemodule.clisitefmodule

import android.annotation.SuppressLint
import android.util.Log
import br.com.softwareexpress.sitef.android.CliSiTef
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.Promise

open class SiTefClient( protected val cliSiTef: CliSiTef)  {
    private var resultHandler: Promise? = null

    @ReactMethod
    fun setResultHandler(promise: Promise) {
        resultHandler = promise
    }

    @SuppressLint("LongLogTag")
    @ReactMethod
    fun success(result: Any?) {
        if (resultHandler != null) {
            resultHandler!!.resolve(result)
        } else {
            Log.d("com.nativemodule.clisitefmodule.SiTefClient::success", result.toString())
        }
    }

    @SuppressLint("LongLogTag")
    @ReactMethod
    fun error(errorCode: String, errorMessage: String?, errorDetails: Any? = null) {
        if (resultHandler != null) {
//            resultHandler!!.invoke(errorCode, errorMessage, errorDetails);
            resultHandler!!.reject(errorCode, errorMessage)
        } else {
            Log.d("com.nativemodule.clisitefmodule.SiTefClient::error::$errorCode", errorMessage.toString())
        }
    }
}

