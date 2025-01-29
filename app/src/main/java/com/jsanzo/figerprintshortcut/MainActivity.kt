package com.jsanzo.figerprintshortcut

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.app.Notification.Builder
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils.SimpleStringSplitter
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jsanzo.figerprintshortcut.ui.MainActivityScreen

class MainActivity : ComponentActivity() {

    private lateinit var notification: Notificacion
    private lateinit var notificationBuilder: Builder

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainActivityScreen(
                enableService = { value -> if (value) enableService() else disableService() },
                enableNotification = {},
                enableInCamera = {},
                onSlideUp = {},
                onSlideDown = {},
                onSlideLeft = {},
                onSlideRight = {}
            )
        }

        notification = Notificacion(this, true);
        notificationBuilder = notification.getNotification("Developed by Jsanzo97®", "FingerShortCut esta activado");


        /*
        if (fragmentManager.findFragmentById(android.R.id.content) == null) {
            fragmentManager.beginTransaction().add(android.R.id.content, SettingsFragment())
                .commit()
        }

         */

        /*
        myService = MyService()
        serviceIntent = Intent(this, MyService::class.java)

        main = this

         */
    }

    fun isAccessibilityOn(context: Context, clazz: Class<*>): Boolean {
        var accessibilityEnabled = 0
        val service = context.packageName + "/" + clazz.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: SettingNotFoundException) {
            e.printStackTrace()
        }

        val colonSplitter = SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                applicationContext.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                colonSplitter.setString(settingValue)
                while (colonSplitter.hasNext()) {
                    val accessibilityService = colonSplitter.next()
                    if (accessibilityService == service) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.canonicalName == service.service.className) {
                return true
            }
        }
        cambiarTextoNotificacion("FingerShortCut esta desactivado")
        return false
    }

    fun showToast(texto: String?) {
        /*
        val inflater = layoutInflater
        val layout = inflater.inflate(
            R.layout.custom_toast,
            findViewById<View>(R.id.custom_toast_container) as ViewGroup
        )

        val text = layout.findViewById<TextView>(R.id.text)
        text.text = texto

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()

         */
    }

    private fun enableService() {
        if (!isAccessibilityOn(applicationContext, MyService::class.java)) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
            showMessage("Active FingerShortCut, por favor")
            notification.showNotification(0, notificationBuilder)
        }
    }

    private fun disableService() {
        if (isMyServiceRunning(MyService::class.java)) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
            showMessage("Desactive FingerShortCut, por favor")
            notification.hideNotification(0)
        }
    }

    private fun showMessage(message: String?) {
        showToast(message)
    }

    private fun cambiarTextoNotificacion(texto: String?) {
        notificationBuilder.setContentText(texto)
        notification.showNotification(0, notificationBuilder)
    }

    public override fun onResume() {
        super.onResume()
        /*
        val editor = prefs!!.edit()
        if (!isMyServiceRunning(MyService::class.java)) {
            editor.putBoolean("switch", false)
            editor.apply()
        } else {
            editor.putBoolean("switch", true)
            editor.apply()
        }
         */
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        /*if(requestCode == Utilities.CAMARA_PHOTO_REQUEST){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Preference camara = SettingsFragment.getInstance().findPreference("checkboxCamara");
            BitmapDrawable bd = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(imageBitmap, 95, 95, true));
            camara.setIcon(bd);
        }*/
        super.onActivityResult(requestCode, resultCode, data)
        showMessage("xrctvybuknlmñ")
    }
}