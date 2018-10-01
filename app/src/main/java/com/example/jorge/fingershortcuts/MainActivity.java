package com.example.jorge.fingershortcuts;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import static com.example.jorge.fingershortcuts.MyService.comprobarUsage;

public class MainActivity extends AppCompatActivity{

    private Intent serviceIntent;
    private MyService myService;

    private static Notificacion n;
    private static Notification.Builder nb;
    private static MainActivity main;

    private final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick = 0L;

    private static SharedPreferences prefs;

    public MainActivity() {
    }

    public static MainActivity getInstance() {
        if(main == null){
            main = new MainActivity();
        }
        return main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        if(getFragmentManager().findFragmentById(android.R.id.content) == null){
            getFragmentManager().beginTransaction().add(android.R.id.content, new SettingsFragment()).commit();
        }

        myService = new MyService();
        serviceIntent = new Intent(this, MyService.class);

        n = new Notificacion(this, true);
        nb = n.getNotification1("Developed by Jsanzo97®", "FingerShortCut esta activado");

        main = this;
    }

    public boolean isAccessibilityOn(Context context, Class<?> clazz){
        int accessibilityEnabled = 0;
        String service = context.getPackageName() + "/" + clazz.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
        if(accessibilityEnabled == 1){
            String settingValue = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if(settingValue != null){
                colonSplitter.setString(settingValue);
                while (colonSplitter.hasNext()){
                    String accessibilityService = colonSplitter.next();
                    if(accessibilityService.equals(service)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClass.getCanonicalName().equals(service.service.getClassName())){
                return true;
            }
        }
        cambiarTextoNotificacion("FingerShortCut esta desactivado");
        return false;
    }

    public void showToast(String texto) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = layout.findViewById(R.id.text);
        text.setText(texto);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void activar(){
        if (!main.isAccessibilityOn(main.getApplicationContext(), MyService.class)) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            main.startActivity(intent);
            showMessage("Active FingerShortCut, por favor");
        }
    }

    public void desactivar(){
        if(main.isMyServiceRunning(MyService.class)) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            main.startActivity(intent);
            showMessage("Desactive FingerShortCut, por favor");
        }
    }

    public void showMessage(String message) {
        main.showToast(message);
    }

    public void mostrarNotificacion(){
        n.notify(0, nb);
    }

    public void ocultarNotificacion(){
        n.getManager().cancel(0);
    }

    public void cambiarTextoNotificacion(String texto){
        nb.setContentText(texto);
        n.notify(0, nb);
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences.Editor editor = prefs.edit();
        if(!isMyServiceRunning(MyService.class)){
            editor.putBoolean("switch", false);
            editor.apply();

        }else{
            editor.putBoolean("switch", true);
            editor.apply();
        }
    }

    @Override
    public void onBackPressed(){
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            showToast("Presione de nuevo para salir");
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        /*if(requestCode == Utilities.CAMARA_PHOTO_REQUEST){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Preference camara = SettingsFragment.getInstance().findPreference("checkboxCamara");
            BitmapDrawable bd = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(imageBitmap, 95, 95, true));
            camara.setIcon(bd);
        }*/
        showMessage("xrctvybuknlmñ");
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

        private static SettingsFragment settings;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesName("settings");
            getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
            addPreferencesFromResource(R.xml.preferences);
            prefs = getPreferenceScreen().getSharedPreferences();
            settings = this;

            Preference p = settings.findPreference("checkboxCamara");
            if(!MainActivity.getInstance().isMyServiceRunning(MyService.class)){
                p.setEnabled(false);
            }else{
                p.setEnabled(true);
            }

            actualizarSummaryAfterPause();
        }

        public static SettingsFragment getInstance(){
            if(settings == null){
                settings = new SettingsFragment();
            }
            return settings;
        }

        private void actualizarSummaryAfterPause(){
            Map<String, ?> ajustes = getPreferenceManager().getSharedPreferences().getAll();
            for(Map.Entry<String, ?> entry : ajustes.entrySet()){
                actualizarSummary(findPreference(entry.getKey()), entry.getValue());
            }
        }

        private void actualizarSummary(Preference preference, Object value) {
            if(preference instanceof ListPreference) {
                String stringValue = value.toString();
                preference.setIconSpaceReserved(true);
                cargarIcono(preference, value.toString());
                ListPreference listPreference = (ListPreference) preference;
                Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object o) {
                        if(o.equals("AbrirAplicacion")) {
                            Intent intent = new Intent(MainActivity.getInstance(), ListaIconos.class);
                            MainActivity.getInstance().startActivity(intent);
                        }
                        return true;
                    }
                };
                listPreference.setOnPreferenceChangeListener(listener);

                if(preference.getKey().equals("listArriba") || preference.getKey().equals("listAbajo") || preference.getKey().equals("listDerecha") || preference.getKey().equals("listIzquierda")){
                    if(value.equals("SubirBrillo") || value.equals("BajarBrillo") || value.equals("AbrirAplicacion") || value.equals("Linterna")) {
                        Preference pareja = getParejaOf(preference);
                        pareja.setDefaultValue("Nada");
                        pareja.setSummary("Nada");
                        pareja.setEnabled(false);
                    } else {
                        Preference pareja = getParejaOf(preference);
                        pareja.setEnabled(true);
                    }
                }

                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

                if(value.equals("AbrirAplicacion")) {
                    if(!prefs.getString("appShortcutPackage", "").equals("Nada") && !prefs.getString("appShortcutName", "").equals("Nada")) {
                        preference.setSummary("Abrir " + prefs.getString("appShortcutName", ""));
                    }else{
                        preference.setSummary("Nada");
                    }
                }

            }else if(preference instanceof CheckBoxPreference) {
                if(preference.getKey().equals("checkbox")) {
                    preference.setIcon(R.drawable.ic_action_name_dark);
                    if (!(Boolean) value) {
                        MainActivity.getInstance().ocultarNotificacion();
                    } else {
                        MainActivity.getInstance().mostrarNotificacion();
                    }
                }else if(preference.getKey().equals("checkboxCamara")){
                    preference.setIcon(R.drawable.camera);
                    if((Boolean) value){
                        if (ContextCompat.checkSelfPermission(MainActivity.getInstance().getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                                ContextCompat.checkSelfPermission(MainActivity.getInstance().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                            ActivityCompat.requestPermissions(MainActivity.getInstance(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                        }
                        if(!comprobarUsage()){
                        MainActivity.getInstance().showMessage("Conceda acceso de uso a FingerShortcut, por favor");
                            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                            startActivity(intent);
                        }
                    }
                }
            }else if(preference instanceof SwitchPreference){
                preference.setIcon(R.mipmap.ic_launcher);
                if((Boolean) value){
                    ((SwitchPreference) preference).setChecked(true);
                    MainActivity.getInstance().activar();
                }else{
                    ((SwitchPreference) preference).setChecked(false);
                    MainActivity.getInstance().desactivar();
                }
            }
        }

        private void cargarIcono(Preference preference, String value){
            switch (value){
                case "AbrirNotificaciones":
                    preference.setIcon(R.drawable.abrir_notificaciones);
                    break;
                case "CerrarNotificaciones":
                    preference.setIcon(R.drawable.cerrar_notificaciones);
                    break;
                case "SubirBrillo":
                    preference.setIcon(R.drawable.subir_brillo);
                    break;
                case "BajarBrillo":
                    preference.setIcon(R.drawable.bajar_brillo);
                    break;
                case "Linterna":
                    preference.setIcon(R.drawable.linterna);
                    break;
                case "AbrirAplicacion":
                    preference.setIcon(getIconoOf(prefs.getString("appShortcutPackage", "")));
                    break;
                case "Nada":
                    preference.setIcon(R.drawable.ic_null);
                    break;
                default:
                    preference.setIcon(null);
                    break;
            }
        }

        public Drawable getIconoOf(String app){
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            PackageManager pm = MainActivity.getInstance().getPackageManager();
            List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);
            System.out.println(app);
            for(final ResolveInfo rInfo : list) {
                if(rInfo.activityInfo.packageName.equals(app)){
                    Drawable icon = rInfo.activityInfo.applicationInfo.loadIcon(pm);
                    Bitmap bmp = Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bmp);
                    icon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    icon.draw(canvas);
                    return new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bmp, 95, 95, true));
                }
            }
            return null;
        }

        private Preference getParejaOf(Preference p){
            String key = p.getKey();
            key += "2";
            return findPreference(key);
        }

        public Preference findPreferenceByValue(String value){
            if(prefs.getString("listArriba", "").equals(value)){
                return findPreference("listArriba");
            }else if(prefs.getString("listArriba2", "").equals(value)){
                return findPreference("listArriba2");
            }else if(prefs.getString("listAbajo", "").equals(value)){
                return findPreference("listAbajo");
            }else if(prefs.getString("listAbajo2", "").equals(value)){
                return findPreference("listAbajo2");
            }else if(prefs.getString("listDerecha", "").equals(value)){
                return findPreference("listDerecha");
            }else if(prefs.getString("listDerecha2", "").equals(value)){
                return findPreference("listDerecha2");
            }else if(prefs.getString("listIzquierda", "").equals(value)){
                return findPreference("listIzquierda");
            }else if(prefs.getString("listIzquierda2", "").equals(value)){
                return findPreference("listIzquierda2");
            }else{
                return null;
            }
        }

        public SharedPreferences getPrefs(){
            return prefs;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            Preference p = findPreference(s);
            prefs = sharedPreferences;
            if(p.getKey().startsWith("list")){
                actualizarSummary(p, prefs.getString(p.getKey(), ""));
            }else{
                actualizarSummary(p, prefs.getBoolean(p.getKey(), true));
            }
        }

        @Override
        public void onResume(){
            super.onResume();
            actualizarSummaryAfterPause();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause(){
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }
    }
}