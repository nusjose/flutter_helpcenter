package com.zendesk_helpcenter

import android.util.Log
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import zendesk.core.JwtIdentity
import zendesk.core.Zendesk
import zendesk.support.CustomField
import zendesk.support.Support
import zendesk.support.request.RequestActivity
import zendesk.support.requestlist.RequestListActivity
import java.util.Arrays

class FlutterZendeskCommonMethod (private val plugin: FlutterZendeskPlugin, private val channel: MethodChannel) {

    companion object {
        const val tag = "[ZendeskMessaging]"
        const val initializeSuccess: String = "initialize_success"

    }

    fun initialize(
        urlString: String,
        appId: String,
        clientId: String,
        nameIdentifier: String,
    ) {

        Zendesk.INSTANCE.init( plugin.activity!!, urlString, appId, clientId)
        Zendesk.INSTANCE.setIdentity(JwtIdentity(nameIdentifier))
        Support.INSTANCE.init(Zendesk.INSTANCE)


        plugin.isInitialize = true
        Log.d("ZendeskInit", "Zendesk initialized with $urlString / $appId")

        channel.invokeMethod(initializeSuccess, null)
    }


    fun showRequestList(call: MethodCall) {
//        val x =  RequestActivity.builder().withCustomFields(Arrays.asList(osPlatForm)).config()
//        RequestListActivity.builder().show(plugin.activity!!)
        if (!Zendesk.INSTANCE.isInitialized) {
            Log.e("Zendesk showRequestList", "Zendesk is not initialized.")
            return
        }

        plugin.activity?.let {
            val intent = RequestListActivity.builder().intent(it)
            it.startActivity(intent)
        }
    }


}