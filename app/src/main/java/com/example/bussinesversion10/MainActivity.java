package com.example.bussinesversion10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btregistro,btventa,btganancia;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

       // AdView adView = new AdView(this);
        //adView.setAdUnitId("ca-app-pub-8242256510253784/7389899693");
        mAdView = (AdView) findViewById(R.id.adView);
       AdRequest adRequest = new AdRequest.Builder().build();
       mAdView.loadAd(adRequest);


        btregistro = (Button)findViewById(R.id.bt_registrar);
        btventa = (Button)findViewById(R.id.bt_venta);
        btganancia = (Button)findViewById(R.id.bt_ganancias);

        btregistro.setOnClickListener(this);
        btventa.setOnClickListener(this);
        btganancia.setOnClickListener(this);

    }
    public void openRegistoScreen(String sc){
        switch (sc){
            case "1":
                Intent intent = new Intent(this, Registro.class);
                startActivity(intent);
                break;
            case "2":
                Intent abrirventas = new Intent(this, Venta.class);
                startActivity(abrirventas);
                break;
            case "3":
                Intent intent3 = new Intent(this, Ganancia.class);
                startActivity(intent3);
                break;
        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_registrar:
                openRegistoScreen("1");
                break;
            case R.id.bt_venta:
                openRegistoScreen("2");
                break;
            case R.id.bt_ganancias:
                openRegistoScreen("3");
                break;
        }
    }
}