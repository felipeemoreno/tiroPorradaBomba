package com.nativemodule;

import android.os.Handler
import android.os.Looper
import android.util.Log
import br.com.softwareexpress.sitef.android.CliSiTef
import br.com.softwareexpress.sitef.android.CliSiTefI
import br.com.softwareexpress.sitef.android.ICliSiTefListener
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.nativemodule.clisitefmodule.TefMethods

class CliSiteFModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    // add to CliSiteF.kt
    override fun getName() = "CliSiteFModule"

    private val handler: Handler = Handler(Looper.getMainLooper())
    private val cliSiTef: CliSiTef = CliSiTef(reactContext)
    private var cliSiTefListener: ICliSiTefListener
    private val tefMethods = TefMethods(cliSiTef)

    init{
        cliSiTef.configure("192.168.0.17", "00000000", "SE000001", "[TipoPinPad=Android_BT]")
        cliSiTefListener = CliSiTefListener(cliSiTef, reactContext)
        cliSiTef.setMessageHandler((cliSiTefListener as CliSiTefListener).onMessage(handler.looper))

        Log.d("cliSiTef version", cliSiTef.version.toString())
    }

    @ReactMethod
    fun ping(text: String) {
        Log.d("CliSiteF", "Ping: $text")
    }

    @ReactMethod
    fun startTransaction(amount: Int) {
        tefMethods.startTransaction(cliSiTefListener, 6, "100", "2", "3", "4", "5")
        Log.d("CliSiteF", "startTransaction value: ${amount.toString()}")
    }

    @ReactMethod
    fun abortTransaction(value: Int) {
        cliSiTef.abortTransaction(value)
        Log.d("CliSiteF", "abortTransaction value: ${value.toString()}")

    }
    @ReactMethod
    fun continueTransaction(data: String) {
        Log.d("CliSiteF", "continueTransaction value: ${data.toString()}")
        cliSiTef.continueTransaction(data)
    }

    @ReactMethod
    fun pinPadMessage(msg: String) {
        cliSiTef.pinpad.setDisplayMessage(msg)
    }

    @ReactMethod
    fun pinPadIsPresent(callback: Callback) {
        val isPresent = cliSiTef.pinpad.isPresent()
        callback.invoke(null, isPresent)
    }

}