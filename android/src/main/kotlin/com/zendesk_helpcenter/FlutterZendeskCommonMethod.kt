package com.zendesk_helpcenter

import android.util.Log
import com.zendesk.logger.Logger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import zendesk.support.Support
import zendesk.support.guide.HelpCenterActivity
import java.util.Locale
import zendesk.core.Zendesk
import zendesk.core.AnonymousIdentity
import zendesk.support.request.RequestActivity
import zendesk.support.requestlist.RequestListActivity
import zendesk.core.JwtIdentity
import zendesk.core.Identity

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
        name: String,
        email: String,
    ) {
        Log.d("ZendeskInit", "start initialize")

        Zendesk.INSTANCE.init(plugin.activity!!.application, urlString, appId, clientId)

//        val identity: Identity = AnonymousIdentity()
//        Zendesk.INSTANCE.setIdentity(identity)

        val identity: Identity = JwtIdentity(email)
        Zendesk.INSTANCE.setIdentity(identity)

//        Zendesk.INSTANCE.setIdentity(
//            AnonymousIdentity.Builder()
//            .withNameIdentifier(name)
//            .withEmailIdentifier(email)
//            .build())

        Support.INSTANCE.init(Zendesk.INSTANCE)

        plugin.isInitialize = true
        Logger.setLoggable(true)

        Log.d("ZendeskInit", "Zendesk initialized with $urlString / $appId")

        channel.invokeMethod(initializeSuccess, null)
    }

    fun updateIdentity(
        name: String,
        email: String,
    ) {
//        val identity = AnonymousIdentity.Builder()
//            .withNameIdentifier(name)
//            .withEmailIdentifier(email)
//            .build()
//        Zendesk.INSTANCE.setIdentity(identity)

        Log.d("ZendeskInit", "updateIdentity - ${name} - ${email}");
        val identity: Identity = JwtIdentity(email)
        Zendesk.INSTANCE.setIdentity(identity)
        Support.INSTANCE.init(Zendesk.INSTANCE)
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
        if (!Zendesk.INSTANCE.isInitialized || !Support.INSTANCE.isInitialized) {
            Log.e(tag, "Zendesk or Support SDK is not initialized.")
            return
        }

        plugin.activity?.let {
            Log.d(tag, "Launching RequestListActivity")
            RequestListActivity.builder().show(it)
        }
    }

    fun showHelpCenterActivity(call: MethodCall) {
        if (!Support.INSTANCE.isInitialized) {
            Log.e(tag, "Zendesk or Support SDK is not initialized.")
            return
        }
        Support.INSTANCE.helpCenterLocaleOverride = Locale("en", "IE")
        plugin.activity?.let {
            Log.d(tag, "Launching showHelpCenterActivity")

            val currentLocale = Support.INSTANCE.helpCenterLocaleOverride ?: Locale.getDefault()
            Log.d("ZendeskLocale", "Help Center Locale: $currentLocale")

            HelpCenterActivity.builder().show(it)
        }
    }

    fun showArticleActivity(call: MethodCall) {
//        if (!Zendesk.INSTANCE.isInitialized || !Support.INSTANCE.isInitialized) {
//            Log.e(tag, "Zendesk or Support SDK is not initialized.")
//            return
//        }
//
//        plugin.activity?.let {
//            Log.d(tag, "Launching showArticleActivity")
//            ViewArticleActivity.builder().show(it)
//        }
    }

    fun showSupportChat(call: MethodCall) {
        if (!Zendesk.INSTANCE.isInitialized || !Support.INSTANCE.isInitialized) {
            Log.e(tag, "Zendesk or Support SDK is not initialized.")
            return
        }
        plugin.activity?.let {
            Log.d(tag, "Launching showSupportChat")
            RequestActivity.builder().show(it);
        }
    }

//    fun showChats(call: MethodCall) {
//        plugin.activity?.let {
//            Log.d(tag, "Launching showChats")
//            zendesk.android.Zendesk.instance.messaging.showMessaging(it)
//        }
//    }

}