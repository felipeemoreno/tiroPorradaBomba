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
import com.nativemodule.clisitefmodule.TefMethods

class CliSiteFModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    // add to CliSiteF.kt
    override fun getName() = "CliSiteFModule"

    private var handler: Handler = Handler(Looper.getMainLooper())
    private val cliSiTef: CliSiTef
    private var cliSiTefListener: ICliSiTefListener


    init{
        cliSiTef = CliSiTef(reactContext);

        cliSiTef.configure("127.0.0.1", "00000000", "SE000001", "[TipoPinPad=Android_BT]")

        cliSiTefListener = CliSiTefListener(cliSiTef, reactContext)

        cliSiTef.setMessageHandler((cliSiTefListener as CliSiTefListener).onMessage(handler.looper))

        Log.d("cliSiTef version", cliSiTef.version.toString())
        Log.d("cliSiTef cliSiTefIV", cliSiTef.cliSiTefIVersion.toString())



    }

    @ReactMethod
    fun ping(text: String) {
        Log.d("CliSiteFModule", "Text from react: $text")
    }

    @ReactMethod
    fun startTransaction(amount: Int) {
        val tefMethods = TefMethods(cliSiTef)

        tefMethods.startTransaction(cliSiTefListener, 6, "100", "2", "3", "4", "5")

        Log.d("CliSiteFModule", "startTransaction value: ${amount.toString()}")
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



//    fun sendMessage(msg: Any) {
//        Log.d("CliSiteFModule", "eventoTeste")
//
//        reactContext
//            .getJSModule<DeviceEventManagerModule.RCTDeviceEventEmitter>(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
//            .emit("eventoTeste", msg)
//    }
}