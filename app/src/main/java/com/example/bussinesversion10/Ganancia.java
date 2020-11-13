package com.example.bussinesversion10;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class Ganancia extends AppCompatActivity{

    private AdView mAdView;
    //private ImageView imv;
    ListView lv;
    ArrayList<producto> infoInventario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganancia);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = (AdView)findViewById(R.id.adView4);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        lv = (ListView)findViewById(R.id.lv_DatosDB);
        infoInventario = consultarListado();

        Adaptadorr adap = new Adaptadorr(getApplicationContext(),infoInventario);
        lv.setAdapter(adap);

    }
    public ArrayList consultarListado() {
        AdminSQLActivities andro = new AdminSQLActivities(this, "administracion", null, 1);
        SQLiteDatabase db = andro.getWritableDatabase();
        ArrayList<producto> ls = new ArrayList<>();
        Bitmap bm = null;
        Cursor select = db.rawQuery("SELECT Codigo,Unidades,Descripcion,imagen FROM producto", null);
        while (select.moveToNext()) {
            byte[] photo = select.getBlob(3);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(photo);
            Bitmap theImage = BitmapFactory.decodeStream(inputStream);
            ls.add(new producto("Codigo: "+select.getString(0),"Unidades: "+select.getString(1),"Descripcion: "+select.getString(2),theImage));
        }
        return ls;
    }

}