package com.jsanzo.figerprintshortcut

import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

object Utilities : AppCompatActivity() {
    var CAMARA_PHOTO_REQUEST: Int = 1

    fun hacerFoto() {
        /*
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            .toString() + "/FingerShortcutCam"
        val fileName = dir + System.currentTimeMillis() + ".jpg"
        val newFile = File(fileName)
        try {
            newFile.createNewFile()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        val output = FileProvider.getUriForFile(
            getInstance().applicationContext,
            getInstance().applicationContext.packageName,
            newFile
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, output)
        println("Hacer foto")
        getInstance().startActivityForResult(cameraIntent, CAMARA_PHOTO_REQUEST)

         */
    }

    fun lanzarApp(service: MyService, name: String) {
        /*
        if (name != "Nada") {
            val intent = Intent(getInstance().packageManager.getLaunchIntentForPackage(name))
            getInstance().startActivity(intent)
            cerrarNotificaciones(service)
        }

         */
    }

    fun linterna(service: MyService, cameraStatus: Boolean): Boolean {
        var cameraStatus = cameraStatus
        val cameraManager = service.getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = cameraManager.cameraIdList[0]
            if (!cameraStatus) {
                cameraManager.setTorchMode(cameraId, true)
                cameraStatus = true
            } else {
                cameraManager.setTorchMode(cameraId, false)
                cameraStatus = false
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        return cameraStatus
    }

    fun abrirNotificaciones(service: MyService) {
        /*if(service.getBarStatus()){
            abrirNotificaciones2(service);
        }else{*/
        @SuppressLint("WrongConstant") val sbservice = service.getSystemService("statusbar")
        val showsb: Method
        try {
            val statusbarManager = Class.forName("android.app.StatusBarManager")
            showsb = statusbarManager.getMethod("expandNotificationsPanel")
            showsb.invoke(sbservice)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        //service.setBarStatus(true);
        //}
    }

    fun abrirNotificaciones2(service: MyService) {
        @SuppressLint("WrongConstant") val sbservice = service.getSystemService("statusbar")
        val showsb: Method
        try {
            val statusbarManager = Class.forName("android.app.StatusBarManager")
            showsb = statusbarManager.getMethod("expandSettingsPanel")
            showsb.invoke(sbservice)
            //service.setBarStatus(false);
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    fun cerrarNotificaciones(service: MyService) {
        @SuppressLint("WrongConstant") val sbservice = service.getSystemService("statusbar")
        val showsb: Method
        try {
            val statusbarManager = Class.forName("android.app.StatusBarManager")
            showsb = statusbarManager.getMethod("collapsePanels")
            showsb.invoke(sbservice)
            //service.setBarStatus(false);
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    fun cambiarBrillo(brillo: Int) {
        /*
        val c = getInstance().applicationContext.contentResolver
        if (Settings.System.canWrite(getInstance().applicationContext)) {
            Settings.System.putInt(
                c,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
            )
            try {
                var brilloActual = Settings.System.getInt(c, Settings.System.SCREEN_BRIGHTNESS)
                var variacion = 8

                if (brilloActual > 64) {
                    variacion = 32
                } else if (brilloActual > 32) {
                    variacion = 16
                }

                if (brillo == 1) {
                    if ((brilloActual + variacion) < 256) {
                        brilloActual += variacion
                    } else {
                        val incremento = 255 - brilloActual
                        brilloActual += incremento
                    }
                    Settings.System.putInt(c, Settings.System.SCREEN_BRIGHTNESS, brilloActual)
                } else if (brillo == -1) {
                    if ((brilloActual - variacion) > 0) {
                        brilloActual -= variacion
                    } else {
                        val decremento = 0 - brilloActual
                        brilloActual += decremento
                    }
                    Settings.System.putInt(c, Settings.System.SCREEN_BRIGHTNESS, brilloActual)
                }
            } catch (e: SettingNotFoundException) {
                e.printStackTrace()
            }
        }

         */
    }
}