package com.nativemodule.clisitefmodule

import android.os.Handler
import android.os.Looper
import android.util.Log
import br.com.softwareexpress.sitef.android.CliSiTef
import br.com.softwareexpress.sitef.android.CliSiTefI
import br.com.softwareexpress.sitef.android.ICliSiTefListener
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.Promise
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

    private val tefMethods:TefMethods = TefMethods(cliSiTef)

    private val pinPadMethods:PinPadMethods = PinPadMethods(cliSiTef)

    private var cliSiTefListener: ICliSiTefListener = CliSiTefListener(cliSiTef, reactContext)
    

    @ReactMethod
    fun configure(
        enderecoSitef: String,
        codigoLoja: String,
        numeroTerminal: String,
        cnpjEmpresa: String,
        cnpjLoja: String
    ) {
        tefMethods.configure(enderecoSitef, codigoLoja, numeroTerminal, "[TipoPinPad=ANDROID_AUTO];[ParmsClient=1=$cnpjLoja;2=$cnpjEmpresa]")
        cliSiTef.setMessageHandler((cliSiTefListener as CliSiTefListener).onMessage(handler.looper))

        Log.d("cliSiTef version", cliSiTef.version.toString())
    }


    @ReactMethod
    fun ping(text: String) {
        Log.d("CliSiteF", "Ping: $text")
    }

    @ReactMethod
    fun setPinpadDisplayMessage(msg: String, promise: Promise) {
        try {
            pinPadMethods.setResultHandler(promise)
            pinPadMethods.setDisplayMessage(msg)
        } catch (e: Throwable) {
            promise.reject("setPinpadDisplayMessage", "Error parsing date", e)
        }
    }

    @ReactMethod
    fun pinpadReadYesNo(msg: String, promise: Promise) {
        try {
            pinPadMethods.setResultHandler(promise)
            pinPadMethods.readYesOrNo(msg)
        } catch (e: Throwable) {
            promise.reject("pinpadReadYesNo", "Error parsing date", e)

        }
    }

    @ReactMethod
    fun pinpadIsPresent( promise: Promise) {
        pinPadMethods.setResultHandler(promise)
        pinPadMethods.isPresent()
    }


    @ReactMethod
    fun startTransaction(amount: Int, promise: Promise) {
        tefMethods.setResultHandler(promise)
        tefMethods.startTransaction(cliSiTefListener, 3, "100", "2", "3", "4", "5")
//        tefMethods.startTransaction(cliSiTefListener, 775, "100", "2", "3", "4", "5")
        Log.d("CliSiteF", "startTransaction value: ${amount.toString()}")
    }

    @ReactMethod
    fun abortTransaction(value: Int) {
        cliSiTef.abortTransaction(value)
        Log.d("CliSiteF", "abortTransaction value: ${value.toString()}")

    }
    @ReactMethod
    fun continueTransaction(data: String, promise: Promise) {

        cliSiTef.continueTransaction(data)
        Log.d("CliSiteF", "continueTransaction value: ${data.toString()}")
    }





}