package com.example.bussinesversion10;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Venta extends AppCompatActivity implements View.OnClickListener{
    private Button bt_consultar,bt_vender;
    private ImageButton bt_escanear;
    private EditText et_codigo,et_descripcion,et_unidades,et_vventa,et_uventa,et_valucompradas;
    public int ventatotal,updateunidades;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //AdView adView = new AdView(this);
        //adView.setAdUnitId("ca-app-pub-8242256510253784/9810165001");
        mAdView = (AdView)findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        bt_escanear=(ImageButton)findViewById(R.id.bt_escan);
        bt_consultar=(Button)findViewById(R.id.bt_cons);
        bt_vender=(Button)findViewById((R.id.bt_venta));

        bt_escanear.setOnClickListener(this);
        bt_consultar.setOnClickListener(this);
        bt_vender.setOnClickListener(this);

        et_codigo = (EditText)findViewById(R.id.txt_cod);
        et_descripcion = (EditText)findViewById(R.id.txt_desc);
        et_descripcion.setEnabled(false);
        et_unidades = (EditText)findViewById(R.id.txt_und);
        et_unidades.setEnabled(false);
        et_valucompradas=(EditText)findViewById(R.id.txt_valcompra);
        et_valucompradas.setEnabled(false);
        et_vventa = (EditText)findViewById(R.id.txt_vventa);
        et_uventa = (EditText)findViewById(R.id.txt_uvender);

    }
    //Metodo para Vender el producto
    public void venderproducto()
    {
        AdminSQLActivities andro = new AdminSQLActivities(this, "administracion", null, 1);
        SQLiteDatabase db = andro.getWritableDatabase();
        String codigo = et_codigo.getText().toString();
        String unidadesdisponibles = et_unidades.getText().toString();
        String PrecioUnidad = et_valucompradas.getText().toString();
        String PrecioVenta = et_vventa.getText().toString(); //uventa
        String unidadesvendidas = et_uventa.getText().toString();

        if(!codigo.isEmpty()&&!PrecioVenta.isEmpty()&&!unidadesvendidas.isEmpty()&&!unidadesvendidas.isEmpty()&&!PrecioUnidad.isEmpty()){
            //convertir a valores enteros para calcular la ganancia
            int vuvent = Integer.parseInt(PrecioVenta);
            int unidada = Integer.parseInt(unidadesdisponibles);
            int unidadv= Integer.parseInt(unidadesvendidas);
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            updateunidades=unidada-unidadv;
            ventatotal = unidadv*vuvent;
            if(updateunidades>=0) {
                ContentValues modificar = new ContentValues();
                ContentValues ventatable = new ContentValues();
                modificar.put("Unidades", updateunidades);
                int cantidad = db.update("producto", modificar, "codigo='" + codigo + "'", null);
                //******
                ventatable.put("Codigo",codigo);
                ventatable.put("Unidades",unidadv);
                ventatable.put("FechaVenta",date);
                ventatable.put("PrecioVenta",vuvent);
                ventatable.put("VentaTotal",ventatotal);
                db.insert("invventa",null,ventatable);
                db.close();
                Toast.makeText(this, "Producto Vendido!", Toast.LENGTH_SHORT).show();
                et_codigo.setText("");
                et_descripcion.setText("");
                et_unidades.setText("");
                et_uventa.setText("");
                et_vventa.setText("");
                et_valucompradas.setText("");
            }else{
                Toast.makeText(this, "Revisa las unidades a Vender e ingresalas nuevamente", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Debes ingresar información en todos los campos", Toast.LENGTH_SHORT).show();
        }


    }
    //Metodo para consultar el producto a vender
    public void consultaproducto()
    {
        AdminSQLActivities andro = new AdminSQLActivities(this, "administracion", null, 1);
        AdminSQLActivities andro2 = new AdminSQLActivities(this, "administracion", null, 1);
        SQLiteDatabase db = andro.getWritableDatabase();
        SQLiteDatabase db2 = andro2.getWritableDatabase();
        String codigo = et_codigo.getText().toString();

        if(!codigo.isEmpty()){
            Cursor fila = db.rawQuery
                    ("SELECT Descripcion, Unidades FROM producto WHERE codigo ='"+codigo+"'", null);
            if(fila.moveToFirst())
            {
                et_descripcion.setText(fila.getString(0));
                et_unidades.setText(fila.getString(1));
                db.close();
                Cursor fila2 = db2.rawQuery("SELECT PrecioUnidad,PrecioVenta FROM invcompra WHERE codigo ='"+codigo+"'",null);
                if(fila2.moveToFirst()){
                    et_valucompradas.setText(fila2.getString(0));
                    et_vventa.setText(fila2.getString(1));
                    db2.close();
                }
            }else{
                Toast.makeText(this, "El producto no existe", Toast.LENGTH_LONG).show();
                db.close();
            }
        }else{
            Toast.makeText(this, "Debes ingresar el código de tu producto", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cons:
                consultaproducto();
                break;
            case R.id.bt_venta:
                venderproducto();
                break;
            case R.id.bt_escan:
                new IntentIntegrator(Venta.this).initiateScan();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        et_codigo.setText(result.getContents());
    }
}