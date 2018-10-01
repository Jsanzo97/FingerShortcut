package com.example.jorge.fingershortcuts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListaIconos extends AppCompatActivity {

    TextView data;
    ImageView image1;
    LinearLayout holdLayout;
    View l1;
    private ArrayList results;
    List<ResolveInfo> list;
    TextView result;
    String str = "";
    Drawable icon;
    Drawable aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_iconos);

        l1 = findViewById(R.id.Layout1);
        results = new ArrayList();
        final PackageManager pm = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);
        for(final ResolveInfo rInfo : list){
            str = "\t" + rInfo.activityInfo.applicationInfo.loadLabel(pm).toString()+ "\n";
            results.add(rInfo.activityInfo.applicationInfo.loadLabel(pm).toString());
            icon = rInfo.activityInfo.applicationInfo.loadIcon(pm);
            holdLayout = new LinearLayout(getApplicationContext());
            holdLayout.setOrientation(LinearLayout.HORIZONTAL);
            data = new TextView(getApplicationContext());
            data.setText(str);
            data.setTextSize(20f);
            image1 = new ImageView(getApplicationContext());
            Bitmap bitmap = getBitmapFromDrawable(icon);
            aux = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 90, 90, true));
            image1.setBackground(aux);
            (holdLayout).addView(image1);
            (holdLayout).addView(data);
            holdLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.getInstance().showMessage("Elegiste: " + rInfo.activityInfo.applicationInfo.loadLabel(pm));
                    SharedPreferences.Editor editor = MainActivity.SettingsFragment.getInstance().getPrefs().edit();
                    editor.putString("appShortcutPackage", rInfo.activityInfo.packageName);
                    editor.apply();
                    editor.putString("appShortcutName", rInfo.activityInfo.applicationInfo.loadLabel(pm).toString());
                    editor.apply();
                    finish();
                }
            });
            ((ViewGroup) l1).addView(holdLayout);
        }
        MainActivity.getInstance().showMessage("Seleccione una");
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable){
        Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    @Override
    public void onBackPressed(){
        SharedPreferences.Editor editor = MainActivity.SettingsFragment.getInstance().getPrefs().edit();
        editor.putString("appShortcutPackage", "Nada");
        editor.apply();
        editor.putString("appShortcutName", "Nada");
        editor.apply();
        super.onBackPressed();
    }

}
