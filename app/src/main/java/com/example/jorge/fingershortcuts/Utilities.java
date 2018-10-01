package com.example.jorge.fingershortcuts;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Utilities extends AppCompatActivity{

    public static int REQUEST_IMAGE_CAP = 1;
    private static String currentPhotoPath;

    /*public static void hacerFoto() {
        System.out.println("Haciendo foto");
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(MainActivity.getApp().getPackageManager()) != null) {
            File photoFile = crearFoto();
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(MainActivity.getApp().getApplicationContext(), MainActivity.getApp().getPackageName(), photoFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                MainActivity.getApp().startActivityForResult(takePicture, REQUEST_IMAGE_CAP);
                guardarFoto();
            }
        }
        System.out.println("Foto hecha: " + currentPhotoPath);
    }

    private static File crearFoto(){
        File image = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "IMG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(fileName, ".jpg", storageDir);
            currentPhotoPath = image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static void guardarFoto(){
        System.out.println("Guardando");
        Intent mediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScan.setData(contentUri);
        MainActivity.getApp().sendBroadcast(mediaScan);
        System.out.println("Guardado: " + currentPhotoPath);
    }*/

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