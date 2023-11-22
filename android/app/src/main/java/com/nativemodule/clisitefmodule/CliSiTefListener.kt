package com.nativemodule;

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import br.com.softwareexpress.sitef.android.CliSiTef
import br.com.softwareexpress.sitef.android.CliSiTefI
import br.com.softwareexpress.sitef.android.ICliSiTefListener
import java.lang.Exception
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.nativemodule.PinPadEvents

class CliSiTefListener(private val cliSiTef: CliSiTef, val reactContext: ReactContext): ICliSiTefListener {

    private var dataSink: DeviceEventManagerModule.RCTDeviceEventEmitter? = null

    private var eventSink: DeviceEventManagerModule.RCTDeviceEventEmitter? = null


    override fun onData(
        currentStage: Int,
        command: Int,
        fieldId: Int,
        minLength: Int,
        maxLength: Int,
        input: ByteArray?
    ) {
        val data = ""
        var clisitefData: CliSiTefData? = null

        when(command) {
            CliSiTef.CMD_SHOW_MSG_CASHIER,
            CliSiTef.CMD_CLEAR_MSG_CASHIER_CUSTOMER,
            CliSiTef.CMD_SHOW_MSG_CASHIER_CUSTOMER,
            CliSiTef.CMD_SHOW_MSG_CUSTOMER -> clisitefData = CliSiTefData(DataEvents.MESSAGE, currentStage, cliSiTef.buffer)
            CliSiTef.CMD_PRESS_ANY_KEY -> clisitefData = CliSiTefData(DataEvents.PRESS_ANY_KEY, currentStage, cliSiTef.buffer)
            CliSiTef.CMD_SHOW_MENU_TITLE,
            CliSiTef.CMD_CLEAR_MENU_TITLE -> clisitefData = CliSiTefData(DataEvents.MENU_TITLE, currentStage, cliSiTef.buffer)
            CliSiTef.CMD_GET_MENU_OPTION -> clisitefData = CliSiTefData(DataEvents.MENU_OPTIONS, currentStage, cliSiTef.buffer, false)
            CliSiTef.CMD_GET_FIELD,
            CliSiTef.CMD_GET_FIELD_BARCODE,
            CliSiTef.CMD_GET_FIELD_CURRENCY -> clisitefData = CliSiTefData(DataEvents.GET_FIELD, currentStage, cliSiTef.buffer, false, maxLength = maxLength, minLength = minLength)
            CliSiTef.CMD_RESULT_DATA -> clisitefData = CliSiTefData(DataEvents.DATA, currentStage, cliSiTef.buffer, true, fieldId)
            else -> Log.i("CliSiTefListener", "onData Default case for command $command")
        }

        if (clisitefData != null) {
            sendMessage(clisitefData.toDataSink())
            if (clisitefData.shouldContinue) {
                cliSiTef.continueTransaction(data)
            }
        } else {
            cliSiTef.continueTransaction(data)
        }
    }

    override fun onTransactionResult(currentStage: Int, resultCode: Int) {
        if (currentStage == 1) {
            if (resultCode == 0) {
                try {
                    cliSiTef.finishTransaction(1)
                    sendMessage(TransactionEvents.TRANSACTION_CONFIRM.named)
                } catch (e: Exception) {
//                    sendMessage(TransactionEvents.TRANSACTION_FAILED.named, e.toString(), e)
                    sendMessage(TransactionEvents.TRANSACTION_FAILED.named)
                }
            }
        } else {
            if (resultCode == 0) {
                sendMessage(TransactionEvents.TRANSACTION_OK.named)
            }
        }
//        sendMessage(TransactionEvents.TRANSACTION_ERROR.named, null, null)
        sendMessage(TransactionEvents.TRANSACTION_ERROR.named)
    }

    fun setEventSink(sink: DeviceEventManagerModule.RCTDeviceEventEmitter?) {
        eventSink = sink
    }

    fun setDataSink(sink: DeviceEventManagerModule.RCTDeviceEventEmitter?) {
        dataSink = sink
    }

    fun onMessage(looper: Looper) = Handler(looper) {
            message ->
        when (message.what) {
            CliSiTefI.EVT_INICIA_ATIVACAO_BT -> sendMessage(PinPadEvents.START_BLUETOOTH.named)
            CliSiTefI.EVT_FIM_ATIVACAO_BT -> sendMessage(PinPadEvents.END_BLUETOOTH.named)
            CliSiTefI.EVT_INICIA_AGUARDA_CONEXAO_PP -> sendMessage(PinPadEvents.WAITING_PINPAD_CONNECTION.named)
            CliSiTefI.EVT_FIM_AGUARDA_CONEXAO_PP -> sendMessage(PinPadEvents.PINPAD_OK.named)
            CliSiTefI.EVT_PP_BT_CONFIGURANDO -> sendMessage(PinPadEvents.WAITING_PINPAD_BLUETOOTH.named)
            CliSiTefI.EVT_PP_BT_CONFIGURADO -> sendMessage(PinPadEvents.PINPAD_BLUETOOTH_CONNECTED.named)
            CliSiTefI.EVT_PP_BT_DESCONECTADO -> sendMessage(PinPadEvents.PINPAD_BLUETOOTH_DISCONNECTED.named)
            else -> sendMessage("Erro generico ${message.what.toString()}")
    //            else -> sendMessage(PinPadEvents.GENERIC_ERROR.named, message.what.toString(), message)
        }
        true
    }

    fun sendMessage(msg: Any) {
        Log.d("cliSiTef MSG", msg.toString())

        reactContext
            .getJSModule<DeviceEventManagerModule.RCTDeviceEventEmitter>(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit("eventoTeste", msg)
    }

}