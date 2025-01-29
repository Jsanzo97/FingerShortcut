package com.jsanzo.figerprintshortcut

import android.Manifest
import android.app.ActivityManager
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.ListPreference
import android.preference.Preference
import android.preference.Preference.OnPreferenceChangeListener
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils.SimpleStringSplitter
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jsanzo.figerprintshortcut.ui.MainActivityScreen

class MainActivity : ComponentActivity() {
    private var serviceIntent: Intent? = null
    private var myService: MyService? = null

    private val INTERVALO = 2000 //2 segundos para salir
    private var tiempoPrimerClick = 0L

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainActivityScreen()
        }

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
    }

    fun activar() {
        if (!main!!.isAccessibilityOn(
                main!!.applicationContext,
                MyService::class.java
            )
        ) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            main!!.startActivity(intent)
            showMessage("Active FingerShortCut, por favor")
        }
    }

    fun desactivar() {
        if (main!!.isMyServiceRunning(MyService::class.java)) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            main!!.startActivity(intent)
            showMessage("Desactive FingerShortCut, por favor")
        }
    }

    fun showMessage(message: String?) {
        main?.showToast(message)
    }

    fun mostrarNotificacion() {
        n!!.notify(0, nb!!)
    }

    fun ocultarNotificacion() {
        n!!.manager?.cancel(0)
    }

    fun cambiarTextoNotificacion(texto: String?) {
        nb!!.setContentText(texto)
        n!!.notify(0, nb!!)
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

    override fun onBackPressed() {
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            showToast("Presione de nuevo para salir")
        }
        tiempoPrimerClick = System.currentTimeMillis()
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

    class SettingsFragment : PreferenceFragment(), OnSharedPreferenceChangeListener {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            addPreferencesFromResource(R.xml.preferences)

            /*
            preferenceManager.sharedPreferencesName = "settings"
            preferenceManager.sharedPreferencesMode = MODE_PRIVATE

            prefs = preferenceScreen.sharedPreferences
            settings = this

            val p = settings!!.findPreference("checkboxCamara")
            if (!getInstance().isMyServiceRunning(
                    MyService::class.java
                )
            ) {
                p.isEnabled = false
            } else {
                p.isEnabled = true
            }

            actualizarSummaryAfterPause()

             */

        }

        private fun actualizarSummaryAfterPause() {
            val ajustes = preferenceManager.sharedPreferences.all
            for ((key, value) in ajustes) {
                actualizarSummary(findPreference(key), value!!)
            }
        }

        private fun actualizarSummary(preference: Preference, value: Any) {
            if (preference is ListPreference) {
                val stringValue = value.toString()
                preference.setIconSpaceReserved(true)
                cargarIcono(preference, value.toString())
                val listPreference = preference
                val listener =
                    OnPreferenceChangeListener { preference, o ->
                        if (o == "AbrirAplicacion") {
                            val intent = Intent(
                                getInstance(),
                                IconsList::class.java
                            )
                            getInstance().startActivity(intent)
                        }
                        true
                    }
                listPreference.onPreferenceChangeListener = listener

                if (preference.getKey() == "listArriba" || preference.getKey() == "listAbajo" || preference.getKey() == "listDerecha" || preference.getKey() == "listIzquierda") {
                    if (value == "SubirBrillo" || value == "BajarBrillo" || value == "AbrirAplicacion" || value == "Linterna") {
                        val pareja = getParejaOf(preference)
                        pareja.setDefaultValue("Nada")
                        pareja.summary = "Nada"
                        pareja.isEnabled = false
                    } else {
                        val pareja = getParejaOf(preference)
                        pareja.isEnabled = true
                    }
                }

                val index = listPreference.findIndexOfValue(stringValue)
                preference.setSummary(if (index >= 0) listPreference.entries[index] else null)

                if (value == "AbrirAplicacion") {
                    if (prefs!!.getString(
                            "appShortcutPackage",
                            ""
                        ) != "Nada" && prefs!!.getString("appShortcutName", "") != "Nada"
                    ) {
                        preference.setSummary("Abrir " + prefs!!.getString("appShortcutName", ""))
                    } else {
                        preference.setSummary("Nada")
                    }
                }
            } else if (preference is CheckBoxPreference) {
                if (preference.getKey() == "checkbox") {
                    preference.setIcon(R.drawable.ic_action_name_dark)
                    if (value !is Boolean) {
                        getInstance().ocultarNotificacion()
                    } else {
                        getInstance().mostrarNotificacion()
                    }
                } else if (preference.getKey() == "checkboxCamara") {
                    preference.setIcon(R.drawable.camera)
                    if (value as Boolean) {
                        if (ContextCompat.checkSelfPermission(
                                getInstance().applicationContext,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_DENIED &&
                            ContextCompat.checkSelfPermission(
                                getInstance().applicationContext,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_DENIED
                        ) {
                            ActivityCompat.requestPermissions(
                                getInstance(),
                                arrayOf(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ),
                                100
                            )
                        }
                        if (!MyService.comprobarUsage()) {
                            getInstance().showMessage("Conceda acceso de uso a FingerShortcut, por favor")
                            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                            startActivity(intent)
                        }
                    }
                }
            } else if (preference is SwitchPreference) {
                preference.setIcon(R.mipmap.ic_launcher)
                if (value as Boolean) {
                    preference.isChecked = true
                    getInstance().activar()
                } else {
                    preference.isChecked = false
                    getInstance().desactivar()
                }
            }
        }

        private fun cargarIcono(preference: Preference, value: String) {
            when (value) {
                "AbrirNotificaciones" -> preference.setIcon(R.drawable.abrir_notificaciones)
                "CerrarNotificaciones" -> preference.setIcon(R.drawable.cerrar_notificaciones)
                "SubirBrillo" -> preference.setIcon(R.drawable.subir_brillo)
                "BajarBrillo" -> preference.setIcon(R.drawable.bajar_brillo)
                "Linterna" -> preference.setIcon(R.drawable.linterna)
                "AbrirAplicacion" -> preference.icon =
                    getIconoOf(prefs!!.getString("appShortcutPackage", ""))

                "Nada" -> preference.setIcon(R.drawable.ic_null)
                else -> preference.icon = null
            }
        }

        fun getIconoOf(app: String?): Drawable? {
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val pm = getInstance().packageManager
            val list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED)
            println(app)
            for (rInfo in list) {
                if (rInfo.activityInfo.packageName == app) {
                    val icon = rInfo.activityInfo.applicationInfo.loadIcon(pm)
                    val bmp = Bitmap.createBitmap(
                        icon.intrinsicWidth,
                        icon.intrinsicHeight,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bmp)
                    icon.setBounds(0, 0, canvas.width, canvas.height)
                    icon.draw(canvas)
                    return BitmapDrawable(resources, Bitmap.createScaledBitmap(bmp, 95, 95, true))
                }
            }
            return null
        }

        private fun getParejaOf(p: Preference): Preference {
            var key = p.key
            key += "2"
            return findPreference(key)
        }

        fun findPreferenceByValue(value: String): Preference? {
            return if (prefs!!.getString("listArriba", "") == value) {
                findPreference("listArriba")
            } else if (prefs!!.getString("listArriba2", "") == value) {
                findPreference("listArriba2")
            } else if (prefs!!.getString("listAbajo", "") == value) {
                findPreference("listAbajo")
            } else if (prefs!!.getString("listAbajo2", "") == value) {
                findPreference("listAbajo2")
            } else if (prefs!!.getString("listDerecha", "") == value) {
                findPreference("listDerecha")
            } else if (prefs!!.getString("listDerecha2", "") == value) {
                findPreference("listDerecha2")
            } else if (prefs!!.getString("listIzquierda", "") == value) {
                findPreference("listIzquierda")
            } else if (prefs!!.getString("listIzquierda2", "") == value) {
                findPreference("listIzquierda2")
            } else {
                null
            }
        }

        fun getPrefs(): SharedPreferences? {
            return prefs
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String?) {
            val p = findPreference(s)
            prefs = sharedPreferences
            if (p.key.startsWith("list")) {
                actualizarSummary(
                    p,
                    prefs!!.getString(p.key, "")!!
                )
            } else {
                actualizarSummary(p, prefs!!.getBoolean(p.key, true))
            }
        }

        override fun onResume() {
            super.onResume()
            /*
            actualizarSummaryAfterPause()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

             */
        }

        override fun onPause() {
            super.onPause()
            /*
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

             */
        }

        companion object {
            private var settings: SettingsFragment? = null

            val instance: SettingsFragment
                get() {
                    if (settings == null) {
                        settings = SettingsFragment()
                    }
                    return settings!!
                }
        }
    }

    companion object {
        private var n: Notificacion? = null
        private var nb: Notification.Builder? = null
        private var main: MainActivity? = null

        private var prefs: SharedPreferences? = null

        fun getInstance(): MainActivity {
            if (main == null) {
                main = MainActivity()
            }
            return main!!
        }
    }
}