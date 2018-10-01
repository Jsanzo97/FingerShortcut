package com.example.jorge.fingershortcuts;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.FingerprintGestureController;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.accessibilityservice.FingerprintGestureController.FINGERPRINT_GESTURE_SWIPE_DOWN;
import static android.accessibilityservice.FingerprintGestureController.FINGERPRINT_GESTURE_SWIPE_LEFT;
import static android.accessibilityservice.FingerprintGestureController.FINGERPRINT_GESTURE_SWIPE_RIGHT;
import static android.accessibilityservice.FingerprintGestureController.FINGERPRINT_GESTURE_SWIPE_UP;

public class MyService extends AccessibilityService {

    private boolean mIsGestureDetectionAvailable;

    private boolean cameraStatus = false;
    //private boolean barStatus = false;
    private String appActual;

    private final int INTERVALO = 500;
    private long tiempoArriba = 0L;
    private long tiempoAbajo = 0L;
    private long tiempoDerecha = 0L;
    private long tiempoIzquierda = 0L;

    private SharedPreferences prefs;

    public MyService() {
    }

    @Override
    public void onCreate() {
        System.out.println("Creado");
    }

    @Override
    protected void onServiceConnected() {
        System.out.println("CONECTADO");
        prefs = MainActivity.SettingsFragment.getInstance().getPrefs();

        Preference p = MainActivity.SettingsFragment.getInstance().findPreference("checkboxCamara");
        p.setEnabled(true);

        MainActivity.getInstance().cambiarTextoNotificacion("FingerShortCut esta activado");
        initGesture();
    }

    private void initGesture(){
        FingerprintGestureController mGestureController = getFingerprintGestureController();
        mIsGestureDetectionAvailable = mGestureController.isGestureDetectionAvailable();

        if (!mIsGestureDetectionAvailable) {
            return;
        }

        FingerprintGestureController.FingerprintGestureCallback mFingerprintGestureCallback = new FingerprintGestureController.FingerprintGestureCallback() {
            @Override
            public void onGestureDetected(int gesture) {
                String pref1, pref2;
                switch (gesture) {
                    case FINGERPRINT_GESTURE_SWIPE_DOWN:
                        pref1 = prefs.getString("listAbajo", "");
                        pref2 = prefs.getString("listAbajo2", "");
                        if (tiempoAbajo + INTERVALO > System.currentTimeMillis()) {
                            deslizDoble(pref1, pref2);
                        } else {
                            deslizSimple(pref1);
                        }
                        tiempoAbajo = System.currentTimeMillis();
                        break;
                    case FINGERPRINT_GESTURE_SWIPE_LEFT:
                        pref1 = prefs.getString("listIzquierda", "");
                        pref2 = prefs.getString("listIzquierda2", "");
                        if (tiempoIzquierda + INTERVALO > System.currentTimeMillis()) {
                            deslizDoble(pref1, pref2);
                        } else {
                            deslizSimple(pref1);
                        }
                        tiempoIzquierda = System.currentTimeMillis();
                        break;
                    case FINGERPRINT_GESTURE_SWIPE_RIGHT:
                        pref1 = prefs.getString("listDerecha", "");
                        pref2 = prefs.getString("listDerecha2", "");
                        if (tiempoDerecha + INTERVALO > System.currentTimeMillis()) {
                            deslizDoble(pref1, pref2);
                        } else {
                            deslizSimple(pref1);
                        }
                        tiempoDerecha = System.currentTimeMillis();
                        break;
                    case FINGERPRINT_GESTURE_SWIPE_UP:
                        pref1 = prefs.getString("listArriba", "");
                        pref2 = prefs.getString("listArriba2", "");
                        if (tiempoArriba + INTERVALO > System.currentTimeMillis()) {
                            deslizDoble(pref1, pref2);
                        } else {
                            deslizSimple(pref1);
                        }
                        tiempoArriba = System.currentTimeMillis();
                        break;
                    default:
                        System.out.println("Error: Unknown gesture type detected!");
                        break;
                }
            }

            @Override
            public void onGestureDetectionAvailabilityChanged(boolean available) {
                mIsGestureDetectionAvailable = available;
            }
        };

        mGestureController.registerFingerprintGestureCallback(mFingerprintGestureCallback, null);
    }

    private void comprobarAccesoEscribirAjustes(){
        if(!Settings.System.canWrite(MainActivity.getInstance().getApplicationContext())){
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:"+this.getPackageName()));
            MainActivity.getInstance().showMessage("Permita modificar ajustes a FingerShortCut");
            startActivity(intent);
        }
    }

    private void deslizSimple(String pref1){
        if(appActual.endsWith(Activity.CAMERA_SERVICE) && prefs.getBoolean("checkboxCamara", true)){
            Utilities.hacerFoto();
        }else{
            dispatcher(pref1);
        }
    }

    private void deslizDoble(String pref1, String pref2){
        if(appActual.endsWith(Activity.CAMERA_SERVICE) && prefs.getBoolean("checkboxCamara", true)){
            return;
        }else{
            if(pref2.equals("Nada")){
                dispatcher(pref1);
            }else{
                dispatcher(pref2);
            }
        }
    }

    private void dispatcher(String action){
        switch (action){
            case "SubirBrillo":
                comprobarAccesoEscribirAjustes();
                Utilities.cambiarBrillo(1);
                break;
            case "BajarBrillo":
                comprobarAccesoEscribirAjustes();
                Utilities.cambiarBrillo(-1);
                break;
            case "AbrirNotificaciones":
                Utilities.abrirNotificaciones(this);
                break;
            case "CerrarNotificaciones":
                Utilities.cerrarNotificaciones(this);
                break;
            case "Linterna":
                cameraStatus = Utilities.linterna(this, cameraStatus);
                break;
            case "AbrirAplicacion":
                String appShortcut = prefs.getString("appShortcutPackage", "");
                Utilities.lanzarApp(this, appShortcut);
                break;
            case "Nada":
                break;
            default:
                Log.d("Dispatcher", "Unknown action");
                break;
        }
    }

    /*public boolean getBarStatus(){
        return barStatus;
    }

    public void setBarStatus(boolean barStatus){
        this.barStatus = barStatus;
    }*/

    private String comprobarPackageAcutual() {
        String currentApp = "Null";
        if(comprobarUsage()) {
            UsageStatsManager usm = (UsageStatsManager) MainActivity.getInstance().getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        }
        return currentApp;
    }

    public static boolean comprobarUsage() {
        try {
            MainActivity m = MainActivity.getInstance();
            PackageManager packageManager = m.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(m.getPackageName(), 0);
            AppOpsManager appOpsManager =(AppOpsManager) m.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e){
            return false;
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        appActual = comprobarPackageAcutual();
    }

    @Override
    public void onDestroy() {
        MainActivity.getInstance().cambiarTextoNotificacion("FingerShortCut esta desactivado");
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getInstance().getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("switch", false);
        editor.apply();
        MainActivity.SettingsFragment.getInstance().findPreference("checkboxCamara").setEnabled(false);
        super.onDestroy();
    }

    @Override
    public void onInterrupt() {
        System.out.println("Interrumpido");
    }
}
