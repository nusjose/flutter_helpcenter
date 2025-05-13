package com.zendesk_helpcenter

import android.util.Log
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import zendesk.core.JwtIdentity
import zendesk.core.Zendesk
import zendesk.support.CustomField
import zendesk.support.Support
import zendesk.support.guide.HelpCenterActivity
import zendesk.support.request.RequestActivity
import zendesk.support.requestlist.RequestListActivity
//import java.util.Arrays
//import zendesk.android.Zendesk
//import zendesk.messaging.android.DefaultMessagingFactory

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

        let context = plugin.activity!!.application

        Zendesk.INSTANCE.init(context, urlString, appId, clientId)
        Zendesk.INSTANCE.setIdentity(JwtIdentity(nameIdentifier))
        Support.INSTANCE.init(Zendesk.INSTANCE)

        plugin.isInitialize = true
        Log.d("ZendeskInit", "Zendesk initialized with $urlString / $appId")

        channel.invokeMethod(initializeSuccess, null)
    }

    fun initializeWithChannel(
        channelKey: String
    ) {
//        Zendesk.initialize(
//            context = plugin.activity!!,
//            channelKey = channelKey,
//            successCallback = { zendesk ->
//                plugin.isInitialize = true
//                Log.i("IntegrationApplication", "Initialization successful")
//            },
//            failureCallback = { error ->
//                // Tracking the cause of exceptions in your crash reporting dashboard will help to triage any unexpected failures in production
//                Log.e("IntegrationApplication", "Initialization failed", error)
//            },
//            messagingFactory = DefaultMessagingFactory()
//        )
    }

    fun showRequestList(call: MethodCall) {
//        val x =  RequestActivity.builder().withCustomFields(Arrays.asList(osPlatForm)).config()
//        RequestListActivity.builder().show(plugin.activity!!)
//        if (!Zendesk.INSTANCE.isInitialized || !Support.INSTANCE.isInitialized) {
//            Log.e(tag, "Zendesk or Support SDK is not initialized.")
//            return
//        }
//
//        plugin.activity?.let {
//            Log.d(tag, "Launching RequestListActivity")
//            val intent = RequestListActivity.builder().intent(it)
//            it.startActivity(intent)
//        }
    }

    fun showHelpCenterActivity(call: MethodCall) {
        if (!Zendesk.INSTANCE.isInitialized || !Support.INSTANCE.isInitialized) {
            Log.e(tag, "Zendesk or Support SDK is not initialized.")
            return
        }

        plugin.activity?.let {
            Log.d(tag, "Launching showHelpCenterActivity")
            HelpCenterActivity.builder().show(it)
        }
    }

}