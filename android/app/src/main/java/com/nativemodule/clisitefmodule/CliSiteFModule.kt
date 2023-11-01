package com.nativemodule

import android.util.Log
import br.com.softwareexpress.sitef.android.CliSiTef
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import android.os.Handler;
import android.os.Looper;

//import com.paulohbsousa.clisitef.channel.DataHandler
//import com.paulohbsousa.clisitef.channel.EventHandler
//import io.flutter.embedding.engine.plugins.FlutterPlugin
//import io.flutter.plugin.common.EventChannel
//import io.flutter.plugin.common.MethodCall
//import io.flutter.plugin.common.MethodChannel
//import io.flutter.plugin.common.MethodChannel.MethodCallHandler
//import io.flutter.plugin.common.MethodChannel.Result


class CliSiteFModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    // add to CliSiteF.kt
    override fun getName() = "CliSiteFModule"

    private val handler: Handler = Handler(Looper.getMainLooper())


    //    private lateinit var methodChannel : Callback
//
    private lateinit var eventChannel : Callback
//
//    private lateinit var dataChannel : EventChannel

    private lateinit var cliSiTef: CliSiTef

//    private lateinit var tefMethods: TefMethods
//
//    private lateinit var pinPadMethods: PinPadMethods;
//
//    private lateinit var cliSiTefListener: CliSiTefListener;
//
//    private val CHANNEL = "com.paulohbsousa.clisitef"

//    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    init{
//        methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, CHANNEL)
//        methodChannel.setMethodCallHandler(this)

        cliSiTef = CliSiTef(reactContext);
//        cliSiTef.setMessageHandler (doSomethingOnBackgroundThread());


//        cliSiTefListener = CliSiTefListener(cliSiTef)
//
//        eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "$CHANNEL/events")
//        eventChannel.setStreamHandler(EventHandler.setListener(cliSiTefListener))
//
//        dataChannel = EventChannel(flutterPluginBinding.binaryMessenger, "$CHANNEL/events/data")
//        dataChannel.setStreamHandler(DataHandler.setListener(cliSiTefListener))
//
//        cliSiTef.setMessageHandler(cliSiTefListener.onMessage(Looper.getMainLooper()));
//
//        tefMethods = TefMethods(cliSiTef)
//        pinPadMethods = PinPadMethods(cliSiTef)
    }

//    @ReactMethod
//    fun doSomethingOnBackgroundThread() {
//        Thread {
//            // Realiza tareas en un hilo separado aquí
//
//            // Puedes usar handler.post() para comunicarte de vuelta al hilo principal
//            handler.post {
//                // Realiza acciones en el hilo principal aquí
//                // Puedes enviar información a JavaScript usando
//                // sendEvent() o Promise.resolve() si es necesario.
//            }
//        }.start()
//    }

//    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
//        tefMethods.setResultHandler(result);
//        pinPadMethods.setResultHandler(result);
//        when (call.method) {
//            "setPinpadDisplayMessage" -> pinPadMethods.setDisplayMessage(call.argument<String>("message")!!)
//            "pinpadReadYesNo" -> pinPadMethods.readYesOrNo(call.argument<String>("message")!!)
//            "pinpadIsPresent" -> pinPadMethods.isPresent()
//            "configure" -> tefMethods.configure(call.argument<String>("enderecoSitef")!!, call.argument<String>("codigoLoja")!!, call.argument<String>("numeroTerminal")!!, "[TipoPinPad=Android_AUTO];[ParmsClient=1="+call.argument<String>("cnpjLoja")!!+";2="+call.argument<String>("cnpjEmpresa")!!+"]")
//            "getQttPendingTransactions" -> tefMethods.getQttPendingTransactions(call.argument<String>("dataFiscal")!!, call.argument<String>("cupomFiscal")!!)
//            "startTransaction" -> tefMethods.startTransaction(cliSiTefListener, call.argument<Int>("modalidade")!!, call.argument<String>("valor")!!, call.argument<String>("cupomFiscal")!!, call.argument<String>("dataFiscal")!!, call.argument<String>("horario")!!, call.argument<String>("operador")!!)
//            "finishLastTransaction" -> tefMethods.finishLastTransaction(call.argument<Int>("confirma")!!)
//            "finishTransaction" -> tefMethods.finishTransaction(call.argument<Int>("confirma")!!, call.argument<String>("cupomFiscal")!!, call.argument<String>("dataFiscal")!!, call.argument<String>("horaFiscal")!!)
//            "abortTransaction" -> tefMethods.abortTransaction(call.argument<Int>("continua")!!)
//            "continueTransaction" -> tefMethods.continueTransaction(call.argument<String>("data")!!)
//            else -> result.notImplemented()
//        }
//    }
//
//    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
//        methodChannel.setMethodCallHandler(null)
//        eventChannel.setStreamHandler(null)
//        dataChannel.setStreamHandler(null)
//    }



    @ReactMethod
    fun ping(text: String) {
        Log.d("CliSiteFModule", "Ping: $text")
    }
}