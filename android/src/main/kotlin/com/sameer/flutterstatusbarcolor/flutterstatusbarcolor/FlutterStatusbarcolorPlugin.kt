package com.sameer.flutterstatusbarcolor.flutterstatusbarcolor

import android.os.Build
import android.view.View
import android.animation.ValueAnimator
import android.app.Activity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.BinaryMessenger


class FlutterStatusbarcolorPlugin private constructor(private val activity: Activity?)  : MethodCallHandler, FlutterPlugin {

    private var methodChannel: MethodChannel? = null
    private lateinit var instance: FlutterStatusbarcolorPlugin

    /** Plugin registration. */
    @SuppressWarnings("deprecation")
    fun registerWith(registrar: Registrar) {
        val instance = FlutterStatusbarcolorPlugin(registrar.activity())
        instance.onAttachedToEngine(registrar.messenger())
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (activity == null) return result.success(null)

        when (call.method) {
            "getstatusbarcolor" -> {
                var statusBarColor = 0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    statusBarColor = activity.window.statusBarColor
                }
                result.success(statusBarColor)
            }
            "setstatusbarcolor" -> {
                val statusBarColor: Int = call.argument("color")!!
                val animate: Boolean = call.argument("animate")!!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animate) {
                        val colorAnim = ValueAnimator.ofArgb(activity.window.statusBarColor, statusBarColor)
                        colorAnim.addUpdateListener { anim -> activity.window.statusBarColor = anim.animatedValue as Int }
                        colorAnim.duration = 300
                        colorAnim.start()
                    } else {
                        activity.window.statusBarColor = statusBarColor
                    }
                }
                result.success(null)
            }
            "setstatusbarwhiteforeground" -> {
                val usewhiteforeground: Boolean = call.argument("whiteForeground")!!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (usewhiteforeground) {
                        activity.window.decorView.systemUiVisibility = activity.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                    } else {
                        activity.window.decorView.systemUiVisibility = activity.window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }
                result.success(null)
            }
            "getnavigationbarcolor" -> {
                var navigationBarColor = 0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    navigationBarColor = activity.window.navigationBarColor
                }
                result.success(navigationBarColor)
            }
            "setnavigationbarcolor" -> {
                val navigationBarColor: Int = call.argument("color")!!
                val animate: Boolean = call.argument("animate")!!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animate) {
                        val colorAnim = ValueAnimator.ofArgb(activity.window.navigationBarColor, navigationBarColor)
                        colorAnim.addUpdateListener { anim -> activity.window.navigationBarColor = anim.animatedValue as Int }
                        colorAnim.duration = 300
                        colorAnim.start()
                    } else {
                        activity.window.navigationBarColor = navigationBarColor
                    }
                }
                result.success(null)
            }
            "setnavigationbarwhiteforeground" -> {
                val usewhiteforeground: Boolean = call.argument("whiteForeground")!!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (usewhiteforeground) {
                        activity.window.decorView.systemUiVisibility = activity.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
                    } else {
                        activity.window.decorView.systemUiVisibility = activity.window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    }
                }
                result.success(null)
            }
            else -> result.notImplemented()
        }
    }

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        onAttachedToEngine(binding.binaryMessenger)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel = null
    }

    private fun onAttachedToEngine(messenger: BinaryMessenger) {
        methodChannel = MethodChannel(messenger, "plugins.sameer.com/statusbar")
        methodChannel!!.setMethodCallHandler(instance)
    }
}
