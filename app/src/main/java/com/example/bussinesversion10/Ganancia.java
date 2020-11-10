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

public class Ganancia extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private Button bttotal,btfecha,btprod;
    private ImageButton bt_excan;
    private EditText et_TotaInv,et_TotaGan,et_anno,et_cood;
    private Spinner Mspinner;
    private String Month;
    private CheckBox xfecha,xproducto,xtotales;
    private LinearLayout lnAno,lnMes,lncod;
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


        bttotal = (Button)findViewById(R.id.bt_totales);
        btfecha = (Button)findViewById(R.id.bt_xfecha);
        btprod = (Button)findViewById(R.id.bt_xprod);
        bt_excan = (ImageButton)findViewById(R.id.bt_scanner);

        xfecha = (CheckBox)findViewById(R.id.chb_xfecha);
        xproducto = (CheckBox)findViewById(R.id.chb_xproducto);
        xtotales = (CheckBox)findViewById(R.id.chb_Totales);


        lnAno=(LinearLayout)findViewById(R.id.lnano);
        lnMes=(LinearLayout)findViewById(R.id.lnmes);
        lncod = (LinearLayout) findViewById(R.id.ln_cod);

        et_TotaInv = (EditText)findViewById(R.id.et_TotalInv);
        et_TotaInv.setEnabled(false);
        et_TotaGan = (EditText)findViewById(R.id.et_TotalGanado);
        et_TotaGan.setEnabled(false);
        et_anno = (EditText)findViewById(R.id.et_ano);
        et_cood = (EditText)findViewById(R.id.et_cod);
        //imv=(ImageView)findViewById(R.id.imageView1);

        Mspinner = (Spinner)findViewById(R.id.SPmonth);
        String[] txtSize = getResources().getStringArray(R.array.Months);
        ArrayAdapter ap = new ArrayAdapter(this, android.R.layout.simple_spinner_item,txtSize);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Mspinner.setAdapter(ap);

        lv = (ListView)findViewById(R.id.lv_DatosDB);
        infoInventario = consultarListado();

        Adaptadorr adap = new Adaptadorr(getApplicationContext(),infoInventario);
        lv.setAdapter(adap);
        /*
        adaptador = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,infoInventario);
        lv.setAdapter(adaptador);
        */


        bttotal.setOnClickListener(this);
        btfecha.setOnClickListener(this);
        btprod.setOnClickListener(this);
        bt_excan.setOnClickListener(this);
        Mspinner.setOnItemSelectedListener(this);
        xfecha.setOnClickListener(this);
        xproducto.setOnClickListener(this);
        xtotales.setOnClickListener(this);


    }

    public void InvVsVendido() {
        AdminSQLActivities andro = new AdminSQLActivities(this, "administracion", null, 1);
        AdminSQLActivities andro2 = new AdminSQLActivities(this, "administracion", null, 1);
        SQLiteDatabase db = andro.getWritableDatabase();
        SQLiteDatabase db2 = andro2.getWritableDatabase();

        Cursor fila = db.rawQuery("SELECT SUM(PrecioTotal) FROM invcompra", null);
        if (fila.moveToFirst()) {
            et_TotaInv.setText(fila.getString(0));
            db.close();
            Cursor fila2 = db2.rawQuery("SELECT SUM(VentaTotal) FROM invventa",null);
            if(fila2.moveToFirst()){
                et_TotaGan.setText(fila2.getString(0));
                db2.close();
            }
        }else{
            Toast.makeText(this,"No Existen Registros",Toast.LENGTH_SHORT).show();
            db.close();
        }
    }

    public void buscarxfecha(){
        AdminSQLActivities andro = new AdminSQLActivities(this, "administracion", null, 1);
        //AdminSQLActivities andro2 = new AdminSQLActivities(this, "administracion", null, 1);
        SQLiteDatabase db = andro.getWritableDatabase();
        //SQLiteDatabase db2 = andro2.getWritableDatabase();
        String ano = et_anno.getText().toString();
        String fecha = ano+"-"+Month;
        if(!ano.isEmpty()){
            Cursor fila = db.rawQuery("SELECT SUM(PrecioTotal) FROM invcompra WHERE FechaCompra LIKE '"+fecha+"%' ",null);
            if(fila.moveToFirst())
            {
                et_TotaInv.setText(fila.getString(0));
            }else{
                Toast.makeText(this,"Dato compra no Existe",Toast.LENGTH_SHORT).show();
                db.close();
            }
            Cursor dateend = db.rawQuery("SELECT SUM(VentaTotal) FROM invventa WHERE FechaVenta LIKE '"+fecha+"%'",null);
            if(dateend.moveToFirst()){
                et_TotaGan.setText(dateend.getString(0));
            }else{
                Toast.makeText(this,"Dato venta no Existe",Toast.LENGTH_SHORT).show();
                db.close();
            }
            db.close();
        }else{
            Toast.makeText(this,"Ingresa el Año que deseas verificar",Toast.LENGTH_LONG).show();
            db.close();
        }
    }

    public void buscarxprod(){
        AdminSQLActivities andro = new AdminSQLActivities(this, "administracion", null, 1);
        SQLiteDatabase db = andro.getWritableDatabase();
        String Cod=et_cood.getText().toString();
        if(!Cod.isEmpty()){
            Cursor dbprod = db.rawQuery("SELECT SUM(PrecioTotal) FROM invcompra WHERE Codigo='"+Cod+"'",null);
            if(dbprod.moveToFirst())
            {
                et_TotaInv.setText(dbprod.getString(0));
            }else{
                Toast.makeText(this,"Producto no existe",Toast.LENGTH_LONG).show();
            }
            Cursor dbventa = db.rawQuery("SELECT SUM(VentaTotal) FROM invventa WHERE Codigo= '"+Cod+"'",null);
            if(dbventa.moveToFirst()){
                et_TotaGan.setText(dbventa.getString(0));
            }else{
                Toast.makeText(this,"Producto no existe",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Ingresa el Código",Toast.LENGTH_LONG).show();
        }


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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_totales:
                InvVsVendido();
                break;
            case R.id.bt_xfecha:
                buscarxfecha();
                break;
            case R.id.bt_xprod:
                buscarxprod();
                break;
            case R.id.chb_xfecha:
                validarxfecha();
                break;
            case R.id.chb_xproducto:
                validarxprod();
                break;
            case R.id.chb_Totales:
                validarxtotal();
                break;
            case R.id.bt_scanner:
                new IntentIntegrator(Ganancia.this).initiateScan();
                break;

        }
    }

    private void validarxtotal() {
        if(xtotales.isChecked()){
            et_TotaInv.setText("");
            et_TotaGan.setText("");
            bttotal.setVisibility(View.VISIBLE);
            lncod.setVisibility(View.GONE);
            btprod.setVisibility(View.GONE);
            btfecha.setVisibility(View.GONE);
            if(xfecha.isChecked()==true)xfecha.setChecked(false);
            if(xproducto.isChecked()==true)xproducto.setChecked(false);
            lnMes.setVisibility(View.GONE);
            lnAno.setVisibility(View.GONE);


        }
    }

    private void validarxprod() {
        if(xproducto.isChecked()){
            et_TotaInv.setText("");
            et_TotaGan.setText("");
            et_cood.setText("");
            btprod.setVisibility(View.VISIBLE);
            lncod.setVisibility(View.VISIBLE);
            lnAno.setVisibility(View.GONE);
            lnMes.setVisibility(View.GONE);
            btfecha.setVisibility(View.GONE);
            bttotal.setVisibility(View.GONE);
            if(xfecha.isChecked()==true)xfecha.setChecked(false);
            if(xtotales.isChecked()==true)xtotales.setChecked(false);


        }
    }

    private void validarxfecha() {
        if(xfecha.isChecked()) {
            et_TotaInv.setText("");
            et_TotaGan.setText("");
            et_anno.setText("");
            btfecha.setVisibility(View.VISIBLE);
            lncod.setVisibility(View.GONE);
            btprod.setVisibility(View.GONE);
            bttotal.setVisibility(View.GONE);
            if(xproducto.isChecked()==true)xproducto.setChecked(false);
            if(xtotales.isChecked()==true)xtotales.setChecked(false);
            lnMes.setVisibility(View.VISIBLE);
            lnAno.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String valuemonth = adapterView.getItemAtPosition(i).toString();
        switch (valuemonth){
            case "Enero":
                Month="01";
                break;
            case "Febrero":
                Month="02";
                break;
            case "Marzo":
                Month="03";
                break;
            case "Abril":
                Month="04";
                break;
            case "Mayo":
                Month="05";
                break;
            case "Junio":
                Month="06";
                break;
            case "Julio":
                Month="07";
                break;
            case "Agosto":
                Month="08";
                break;
            case "Septiembre":
                Month="09";
                break;
            case "Octubre":
                Month="10";
                break;
            case "Noviembre":
                Month="11";
                break;
            case "Diciembre":
                Month="12";
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            Uri Path = data.getData();
            //imagen.setImageURI(Path);
        }else{
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            et_cood.setText(result.getContents());
        }


    }
}