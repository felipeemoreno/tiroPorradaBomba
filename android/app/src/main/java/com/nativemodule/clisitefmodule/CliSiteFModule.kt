package com.nativemodule;

import android.os.Handler
import android.os.Looper
import android.util.Log
import br.com.softwareexpress.sitef.android.CliSiTef
import br.com.softwareexpress.sitef.android.CliSiTefI
import br.com.softwareexpress.sitef.android.ICliSiTefListener
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule

class CliSiteFModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    // add to CliSiteF.kt
    override fun getName() = "CliSiteFModule"

    private var handler: Handler = Handler(Looper.getMainLooper())
    private val cliSiTef: CliSiTef
    private var cliSiTefListener: ICliSiTefListener

    init{
        cliSiTef = CliSiTef(reactContext);
        cliSiTef.configure("127.0.0.1", "0", "1","0")
        cliSiTefListener = CliSiTefListener(cliSiTef, reactContext)

//        cliSiTef.setMessageHandler(onMessage(handler.looper))

        Log.d("cliSiTef V", cliSiTef.version.toString())

    }

//    fun run() {
//        Looper.prepare()
//        this.handler = Handler(checkNotNull(Looper.myLooper())) { msg: Message ->
//            Log.d("cliSiTef MSG", msg.toString())
//            true
//        }
//        Looper.loop()
//    }

//    fun onMessage(looper: Looper) = Handler(looper) {
//            message ->
//        when (message.what) {
//            CliSiTefI.EVT_INICIA_ATIVACAO_BT -> sendMessage(PinPadEvents.START_BLUETOOTH.named)
//            CliSiTefI.EVT_FIM_ATIVACAO_BT -> sendMessage(PinPadEvents.END_BLUETOOTH.named)
//            CliSiTefI.EVT_INICIA_AGUARDA_CONEXAO_PP -> sendMessage(PinPadEvents.WAITING_PINPAD_CONNECTION.named)
//            CliSiTefI.EVT_FIM_AGUARDA_CONEXAO_PP -> sendMessage(PinPadEvents.PINPAD_OK.named)
//            CliSiTefI.EVT_PP_BT_CONFIGURANDO -> sendMessage(PinPadEvents.WAITING_PINPAD_BLUETOOTH.named)
//            CliSiTefI.EVT_PP_BT_CONFIGURADO -> sendMessage(PinPadEvents.PINPAD_BLUETOOTH_CONNECTED.named)
//            CliSiTefI.EVT_PP_BT_DESCONECTADO -> sendMessage(PinPadEvents.PINPAD_BLUETOOTH_DISCONNECTED.named)
//            else -> sendMessage("Erro generico ${message.what.toString()}")
//            //            else -> sendMessage(PinPadEvents.GENERIC_ERROR.named, message.what.toString(), message)
//        }
//        true
//    }

    @ReactMethod
    fun ping(text: String) {
        cliSiTef.startTransaction(cliSiTefListener, 6, "100", "2", "3", "4", "5", "6")
        Log.d("CliSiteFModule", "Ping: $text")
    }

//    fun sendMessage(msg: Any) {
//        Log.d("CliSiteFModule", "eventoTeste")
//
//        reactContext
//            .getJSModule<DeviceEventManagerModule.RCTDeviceEventEmitter>(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
//            .emit("eventoTeste", msg)
//    }
}