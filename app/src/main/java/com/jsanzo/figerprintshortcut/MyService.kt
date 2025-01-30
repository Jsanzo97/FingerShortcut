package com.jsanzo.figerprintshortcut

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.FingerprintGestureController
import android.accessibilityservice.FingerprintGestureController.FingerprintGestureCallback
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager
import android.hardware.fingerprint.FingerprintManager
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import java.util.SortedMap
import java.util.TreeMap

class MyService : AccessibilityService() {
    private var mIsGestureDetectionAvailable = false

    //private boolean barStatus = false;
    private var appActual: String? = null


    override fun onCreate() {
        println("Creado")
    }

    override fun onServiceConnected() {
        println("CONECTADO")
        initGesture()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun initGesture() {
        val fingerManager = getSystemService(BIOMETRIC_SERVICE) as BiometricManager


        val mGestureController = fingerprintGestureController
        mIsGestureDetectionAvailable = mGestureController.isGestureDetectionAvailable

        if (!mIsGestureDetectionAvailable) {
            return
        }

        val mFingerprintGestureCallback: FingerprintGestureCallback =
            object : FingerprintGestureCallback() {
                override fun onGestureDetected(gesture: Int) {
                    when (gesture) {
                        FingerprintGestureController.FINGERPRINT_GESTURE_SWIPE_DOWN -> {
                            Utilities.linterna(this@MyService, false)
                            println("DOWN")
                        }

                        FingerprintGestureController.FINGERPRINT_GESTURE_SWIPE_LEFT -> {
                            Utilities.linterna(this@MyService, false)
                            println("LEFT")
                        }

                        FingerprintGestureController.FINGERPRINT_GESTURE_SWIPE_RIGHT -> {
                            Utilities.linterna(this@MyService, false)
                            println("RIGHT")

                        }

                        FingerprintGestureController.FINGERPRINT_GESTURE_SWIPE_UP -> {
                            Utilities.linterna(this@MyService, false)
                            println("UP")

                        }

                        else -> println("Error: Unknown gesture type detected!")
                    }
                }

                override fun onGestureDetectionAvailabilityChanged(available: Boolean) {
                    mIsGestureDetectionAvailable = available
                }
            }

        mGestureController.registerFingerprintGestureCallback(mFingerprintGestureCallback, null)
    }

    private fun comprobarAccesoEscribirAjustes() {
        if (!Settings.System.canWrite(applicationContext)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.setData(Uri.parse("package:" + this.packageName))
            //MainActivity.getInstance().showMessage("Permita modificar ajustes a FingerShortCut")
            startActivity(intent)
        }
    }

    private fun deslizSimple(pref1: String) {
        /*
        val prefs = MainActivity.SettingsFragment.instance.getPrefs()

        if (appActual!!.endsWith(CAMERA_SERVICE) && prefs!!.getBoolean("checkboxCamara", true)) {
            Utilities.hacerFoto()
        } else {
            dispatcher(pref1)
        }

         */
    }

    private fun deslizDoble(pref1: String, pref2: String) {
        /*
        val prefs = MainActivity.SettingsFragment.instance.getPrefs()

        if (appActual!!.endsWith(CAMERA_SERVICE) && prefs!!.getBoolean("checkboxCamara", true)) {
            return
        } else {
            if (pref2 == "Nada") {
                dispatcher(pref1)
            } else {
                dispatcher(pref2)
            }
        }

         */
    }

    private fun dispatcher(action: String) {
        /*
        val prefs = SettingsFragment.instance.getPrefs()

        when (action) {
            "SubirBrillo" -> {
                comprobarAccesoEscribirAjustes()
                Utilities.cambiarBrillo(1)
            }

            "BajarBrillo" -> {
                comprobarAccesoEscribirAjustes()
                Utilities.cambiarBrillo(-1)
            }

            "AbrirNotificaciones" -> Utilities.abrirNotificaciones(
                this
            )

            "CerrarNotificaciones" -> Utilities.cerrarNotificaciones(
                this
            )

            "Linterna" -> cameraStatus = Utilities.linterna(this, cameraStatus)
            "AbrirAplicacion" -> {
                val appShortcut = prefs!!.getString("appShortcutPackage", "")!!
                Utilities.lanzarApp(this, appShortcut)
            }

            "Nada" -> {}
            else -> Log.d("Dispatcher", "Unknown action")
        }

         */
    }

    /*public boolean getBarStatus(){
        return barStatus;
    }

    public void setBarStatus(boolean barStatus){
        this.barStatus = barStatus;
    }*/
    private fun comprobarPackageAcutual(): String {
        /*
        var currentApp = "Null"
        if (comprobarUsage()) {
            val usm = MainActivity.getInstance()
                .getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
            val time = System.currentTimeMillis()
            val appList =
                usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time)
            if (appList != null && appList.size > 0) {
                val mySortedMap: SortedMap<Long, UsageStats> = TreeMap()
                for (usageStats in appList) {
                    mySortedMap[usageStats.lastTimeUsed] = usageStats
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap[mySortedMap.lastKey()]!!.packageName
                }
            }
        }
        return currentApp

         */
        return ""
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        appActual = comprobarPackageAcutual()
        val nodo = event.source
        if (nodo != null) {
            nodo.refresh()
            println(nodo.packageName.toString() + " " + nodo.childCount)
            for (i in 0 until nodo.childCount) {
                if (nodo.getChild(i).contentDescription != null && nodo.getChild(i).contentDescription.toString()
                        .startsWith("Bot")
                ) {
                    println(nodo.getChild(i).contentDescription.toString() + " " + nodo.getChild(i).viewIdResourceName)
                }
            }
        }
    }

    override fun onDestroy() {
        /*
        MainActivity.getInstance().cambiarTextoNotificacion("FingerShortCut esta desactivado")
        val prefs = PreferenceManager.getDefaultSharedPreferences(
            MainActivity.getInstance().applicationContext
        )
        val editor = prefs.edit()
        editor.putBoolean("switch", false)
        editor.apply()
        MainActivity.SettingsFragment.instance.findPreference("checkboxCamara")
            .setEnabled(false)
        super.onDestroy()

         */
    }

    override fun onInterrupt() {
        println("Interrumpido")
    }

    companion object {
        fun comprobarUsage(context: Context): Boolean {
            try {
                val packageManager: PackageManager = context.packageManager
                val applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
                val appOpsManager = context.getSystemService(APP_OPS_SERVICE) as AppOpsManager
                val mode = appOpsManager.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    applicationInfo.uid,
                    applicationInfo.packageName
                )
                return (mode == AppOpsManager.MODE_ALLOWED)
            } catch (e: PackageManager.NameNotFoundException) {
                return false
            }
        }
    }
}