package com.jsanzo.figerprintshortcut

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class IconsList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_iconos)

        val l1: View = findViewById(R.id.Layout1)

        val pm = this.packageManager

        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        for (rInfo in pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED)) {
            val str = "\t" + rInfo.activityInfo.applicationInfo.loadLabel(pm).toString() + "\n"

            val holdLayout = LinearLayout(applicationContext)
            holdLayout.orientation = LinearLayout.HORIZONTAL

            val data = TextView(applicationContext)
            data.text = str
            data.textSize = 20f

            val icon = rInfo.activityInfo.applicationInfo.loadIcon(pm)
            val bitmap = getBitmapFromDrawable(icon)

            val aux = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 90, 90, true))

            val image1 = ImageView(applicationContext)
            image1.background = aux

            holdLayout.addView(image1)
            holdLayout.addView(data)
            holdLayout.setOnClickListener {
                MainActivity.getInstance()
                    .showMessage("Elegiste: " + rInfo.activityInfo.applicationInfo.loadLabel(pm))
                val editor = MainActivity.SettingsFragment.instance.getPrefs()?.edit()!!
                editor.putString("appShortcutPackage", rInfo.activityInfo.packageName)
                editor.apply()
                editor.putString(
                    "appShortcutName",
                    rInfo.activityInfo.applicationInfo.loadLabel(pm).toString()
                )
                editor.apply()
                finish()
            }

            (l1 as ViewGroup).addView(holdLayout)
        }

        MainActivity.getInstance().showMessage("Seleccione una")
    }

    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        val bmp = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bmp)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bmp
    }

    override fun onBackPressed() {
        val editor = MainActivity.SettingsFragment.instance.getPrefs()?.edit()!!
        editor.putString("appShortcutPackage", "Nada")
        editor.apply()
        editor.putString("appShortcutName", "Nada")
        editor.apply()
        super.onBackPressed()
    }
}