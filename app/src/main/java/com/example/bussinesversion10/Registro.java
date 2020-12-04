package com.example.bussinesversion10;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Registro extends AppCompatActivity implements View.OnClickListener{
    private Button bt_guardar,bt_modificar,bt_consultar;
    private ImageButton bt_imagen,bt_escanear,bt_agregar,bt_quitar;
    private EditText et_codigo,et_descripcion,et_vcompra,et_unidades,et_vventa;
    private ImageView imagen;
    private LinearLayout ln;
    public int valorunidadcom,TotalCompra,unid,valoruniventa;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

       // AdView adView = new AdView(this);
        //adView.setAdUnitId("ca-app-pub-8242256510253784/2314818363");
        mAdView = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        bt_escanear=(ImageButton)findViewById(R.id.bt_escanear);
        bt_guardar=(Button)findViewById(R.id.bt_guardar);
        bt_modificar=(Button)findViewById(R.id.bt_modificar);
        bt_consultar=(Button)findViewById(R.id.bt_cons);
        bt_imagen=(ImageButton)findViewById(R.id.bt_imagen);
        bt_agregar=(ImageButton)findViewById(R.id.imageButton);
        bt_quitar=(ImageButton)findViewById(R.id.imageButton2);


        bt_guardar.setOnClickListener(this);
        bt_modificar.setOnClickListener(this);
        bt_escanear.setOnClickListener(this);
        bt_consultar.setOnClickListener(this);
        bt_imagen.setOnClickListener(this);
        bt_agregar.setOnClickListener(this);
        bt_quitar.setOnClickListener(this);

        et_codigo = (EditText)findViewById(R.id.txt_codigo);
        et_descripcion = (EditText)findViewById(R.id.txt_descripcion);
        et_vcompra = (EditText)findViewById(R.id.txt_vcompra);
        et_unidades = (EditText)findViewById(R.id.txt_unidades);
        et_vventa = (EditText)findViewById(R.id.txt_Vventa);

        ln = (LinearLayout)findViewById(R.id.lnImage);

        imagen=(ImageView)findViewById(R.id.imgProducto);

    }
    //Método para guardar producto
    public void guardarproducto(){
        AdminSQLActivities andro = new AdminSQLActivities(this, "administracion", null, 1);
        SQLiteDatabase db = andro.getWritableDatabase();
        String codigo = et_codigo.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String vucompra = et_vcompra.getText().toString();
        String unidades = et_unidades.getText().toString();
        String vuventa = et_vventa.getText().toString();

        if(!codigo.isEmpty()&&!descripcion.isEmpty()&&!vucompra.isEmpty()&&!unidades.isEmpty()&&!vuventa.isEmpty()){
//            Cursor maxregistros = db.rawQuery("SELECT COUNT(Codigo) from producto",null); //PARA LIMITAR EL NUMERO DE REGISTROS EN LA APP
//            maxregistros.moveToFirst();
//            if(maxregistros.getInt(0)!=100) {

                unid = Integer.parseInt(unidades);
                valorunidadcom = Integer.parseInt(vucompra);
                TotalCompra = unid * valorunidadcom;
                valoruniventa = Integer.parseInt(vuventa);
                if (!imagen.isShown()) {
                    imagen.setImageResource(R.mipmap.noproduct);
                }
                byte[] NewEntryImg = imgeViewToByte(imagen);
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                ContentValues guardarprod = new ContentValues();
                ContentValues invecompra = new ContentValues();
                Cursor prod = db.rawQuery("SELECT Descripcion FROM producto WHERE Codigo='" + codigo + "'", null);
                if (prod.moveToFirst() == false) {
                    //**
                    guardarprod.put("Codigo", codigo);
                    guardarprod.put("Descripcion", descripcion);
                    guardarprod.put("unidades", unid);
                    guardarprod.put("imagen", NewEntryImg);
                    //**
                    invecompra.put("Codigo", codigo);
                    invecompra.put("FechaCompra", date);
                    invecompra.put("PrecioUnidad", valorunidadcom);
                    invecompra.put("PrecioVenta", valoruniventa);
                    invecompra.put("PrecioTotal", TotalCompra);
                    db.insert("producto", null, guardarprod);
                    db.insert("invcompra", null, invecompra);
                    db.close();
                    et_codigo.setText("");
                    et_descripcion.setText("");
                    et_vcompra.setText("");
                    et_unidades.setText("1");
                    et_vventa.setText("");
                    ln.setVisibility(View.GONE);
                    Toast.makeText(this, "Producto guardado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Producto existente, modificalo para Actualizar Stock", Toast.LENGTH_SHORT).show();
                }

//       } else{
//                Toast.makeText(this, "Version de prueba finalizada actualiza a version pro", Toast.LENGTH_LONG).show();
//            }
        }else{
            Toast.makeText(this, "Debes ingresar información en todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    private byte[] imgeViewToByte(ImageView img) {
        Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP,0,stream);
        byte[] bytearray = stream.toByteArray();
        return bytearray;
    }

    //Metidi para modificar
    public void modificarproducto()
    {
        AdminSQLActivities andro = new AdminSQLActivities(this, "administracion", null, 1);
        SQLiteDatabase db = andro.getWritableDatabase();
        String codigo = et_codigo.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String vucompra = et_vcompra.getText().toString();
        String vuventa = et_vventa.getText().toString();
        String unidades = et_unidades.getText().toString();

        if(!codigo.isEmpty()&&!descripcion.isEmpty()&&!vucompra.isEmpty()&&!unidades.isEmpty()&&!vuventa.isEmpty()&&imagen.isShown()){
            unid = Integer.parseInt(unidades);
            valorunidadcom = Integer.parseInt(vucompra);
            TotalCompra= unid*valorunidadcom;
            valoruniventa = Integer.parseInt(vuventa);
            byte[] NewEntryImg = imgeViewToByte(imagen);
            ContentValues modificar = new ContentValues();
            ContentValues modinvcompra = new ContentValues();
            modificar.put("Descripcion",descripcion);
            modificar.put("Unidades",unid);
            modificar.put("imagen",NewEntryImg);
            //modinvcompra.put("Unidades",unid);
            modinvcompra.put("PrecioUnidad",valorunidadcom);
            modinvcompra.put("PrecioVenta",valoruniventa);
            modinvcompra.put("PrecioTotal",TotalCompra);
            int cantidad = db.update("producto",modificar,"Codigo= '"+codigo+"' ",null);
            int cantidad2 = db.update("invcompra",modinvcompra,"Codigo='"+codigo+"'",null);
            db.close();
            Toast.makeText(this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();
            imagen.setImageResource(android.R.color.transparent);
            ln.setVisibility(View.GONE);
            et_codigo.setText("");
            et_descripcion.setText("");
            et_vcompra.setText("");
            et_unidades.setText("1");
            et_vventa.setText("");
            bt_modificar.setEnabled(false);
            bt_modificar.setTextColor(Color.parseColor("#F1DA95"));
        }else{
            Toast.makeText(this, "Debes ingresar información en todos los campos e Imagen", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para consultar un producto
    public void consultarproducto()
    {
        AdminSQLActivities andro = new AdminSQLActivities(this, "administracion", null, 1);
        AdminSQLActivities andro2 = new AdminSQLActivities(this, "administracion", null, 1);
        SQLiteDatabase db = andro.getWritableDatabase();
        SQLiteDatabase db2 = andro2.getWritableDatabase();
        String codigo = et_codigo.getText().toString();
        Bitmap bm = null;
        if(!codigo.isEmpty()){
            Cursor fila = db.rawQuery("SELECT Descripcion, Unidades, imagen FROM producto WHERE Codigo ='"+codigo+"'", null);
            if(fila.moveToFirst())
            {

                et_descripcion.setText(fila.getString(0));
                et_unidades.setText(fila.getString(1));
                byte[] photo = fila.getBlob(2);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(photo);
                Bitmap theImage = BitmapFactory.decodeStream(inputStream);
                ln.setVisibility(View.VISIBLE);
                imagen.setImageBitmap(theImage);
                db.close();
                bt_modificar.setEnabled(true);
                bt_modificar.setTextColor(Color.parseColor("#F4C128"));
                Cursor fila2 = db2.rawQuery("SELECT PrecioUnidad,PrecioVenta from invcompra WHERE Codigo ='"+codigo+"'",null);
                if(fila2.moveToFirst())
                {
                    et_vcompra.setText(fila2.getString(0));
                    et_vventa.setText(fila2.getString(1));
                    db2.close();
                }
            }else{
                Toast.makeText(this, "El producto no existe", Toast.LENGTH_LONG).show();
                db.close();
            }
        }else{
            Toast.makeText(this, "Debes ingresar el código de tu producto", Toast.LENGTH_SHORT).show();
            db.close();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_guardar:
                guardarproducto();
                break;
            case R.id.bt_cons:
                consultarproducto();
                break;
            case R.id.bt_modificar:
                modificarproducto();
                break;
            case R.id.bt_escanear:
                new IntentIntegrator(Registro.this).initiateScan();
                break;
            case R.id.bt_imagen:
                cargarImagen();
                break;
            case R.id.imageButton:
                adduni();
                break;
            case R.id.imageButton2:
                deluni();
                break;
        }
    }

    private void deluni() {
        int valor1 = Integer.parseInt(et_unidades.getText().toString());
        valor1--;
        if(valor1==0)valor1=1;
        String value = Integer.toString(valor1);
        et_unidades.setText(value);
    }

    private void adduni() {
        int valor1 = Integer.parseInt(et_unidades.getText().toString());
        valor1++;
        String value = Integer.toString(valor1);
        et_unidades.setText(value);
    }

    private void cargarImagen() {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/");
            startActivityForResult(intent.createChooser(intent,"Selecciona la aplicación"),1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!(resultCode == 0)) {
            if (requestCode == 1) {
                Uri Path = data.getData();
                imagen.setImageURI(Path);
                ln.setVisibility(View.VISIBLE);
            } else if (requestCode == 49374) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                et_codigo.setText(result.getContents());
            }
        }
    }
}