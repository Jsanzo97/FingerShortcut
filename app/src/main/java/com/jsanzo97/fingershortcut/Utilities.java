package com.jsanzo97.fingershortcut;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Utilities extends AppCompatActivity{

    public static int CAMARA_PHOTO_REQUEST = 1;

    public static void hacerFoto() {

    }

    public static void lanzarApp(MyService service, String name){
        if(!name.equals("Nada")){
            Intent intent = new Intent(MainActivity.getInstance().getPackageManager().getLaunchIntentForPackage(name));
            MainActivity.getInstance().startActivity(intent);
            Utilities.cerrarNotificaciones(service);
        }
    }

    public static boolean linterna(MyService service, boolean cameraStatus){
        CameraManager cameraManager = (CameraManager) service.getSystemService(CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            if(!cameraStatus) {
                cameraManager.setTorchMode(cameraId, true);
                cameraStatus = true;
            }else{
                cameraManager.setTorchMode(cameraId, false);
                cameraStatus = false;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return cameraStatus;
    }

    public static void abrirNotificaciones(MyService service){
        /*if(service.getBarStatus()){
            abrirNotificaciones2(service);
        }else{*/
            @SuppressLint("WrongConstant") Object sbservice = service.getSystemService("statusbar");
            Method showsb;
            try {
                Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
                showsb = statusbarManager.getMethod("expandNotificationsPanel");
                showsb.invoke(sbservice);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            //service.setBarStatus(true);
        //}
    }

    public static void abrirNotificaciones2(MyService service){
        @SuppressLint("WrongConstant") Object sbservice = service.getSystemService("statusbar");
        Method showsb;
        try {
            Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
            showsb = statusbarManager.getMethod("expandSettingsPanel");
            showsb.invoke(sbservice);
            //service.setBarStatus(false);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void cerrarNotificaciones(MyService service){
        @SuppressLint("WrongConstant") Object sbservice = service.getSystemService("statusbar");
        Method showsb;
        try {
            Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
            showsb = statusbarManager.getMethod("collapsePanels");
            showsb.invoke(sbservice);
            //service.setBarStatus(false);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void cambiarBrillo(int brillo){
        ContentResolver c = MainActivity.getInstance().getApplicationContext().getContentResolver();
        if(Settings.System.canWrite(MainActivity.getInstance().getApplicationContext())) {
            Settings.System.putInt(c, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            try {
                int brilloActual = Settings.System.getInt(c, Settings.System.SCREEN_BRIGHTNESS);
                int variacion = 8;

                if (brilloActual > 64) {
                    variacion = 32;
                } else if (brilloActual > 32) {
                    variacion = 16;
                }

                if (brillo == 1) {
                    if ((brilloActual + variacion) < 256) {
                        brilloActual += variacion;
                    } else {
                        int incremento = 255 - brilloActual;
                        brilloActual += incremento;
                    }
                    Settings.System.putInt(c, Settings.System.SCREEN_BRIGHTNESS, brilloActual);
                } else if (brillo == -1) {
                    if ((brilloActual - variacion) > 0) {
                        brilloActual -= variacion;
                    } else {
                        int decremento = 0 - brilloActual;
                        brilloActual += decremento;
                    }
                    Settings.System.putInt(c, Settings.System.SCREEN_BRIGHTNESS, brilloActual);
                }
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}