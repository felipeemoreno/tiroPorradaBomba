package com.nativemodule.calendarmodule;

import android.util.Log
import android.view.Gravity
import java.util.UUID
import android.widget.Toast
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class CalendarModule(val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    // add to CalendarModule.kt
    override fun getName() = "CalendarModule"

    @ReactMethod
    fun createCalendarEvent(name: String, location: String, promise: Promise) {
        Log.d("CalendarModule", "Create event called with name: $name and location: $location")
        try {
            val myUuid = UUID.randomUUID()
            val eventId = myUuid.toString()
            promise.resolve(eventId)
        } catch (e: Throwable) {
            promise.reject("Create Event error", "Error parsing date", e)
        }
    }

    override fun getConstants(): MutableMap<String, Any> =
        hashMapOf("DEFAULT_EVENT_NAME" to "New Event")

    @ReactMethod
    fun showToast(message: String) {
        val toast = Toast.makeText(reactContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0, )
        toast.show()
    }





}