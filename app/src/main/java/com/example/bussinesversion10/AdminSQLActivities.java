package com.example.bussinesversion10;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class AdminSQLActivities extends SQLiteOpenHelper{
    public AdminSQLActivities(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String prodTable="CREATE TABLE producto(Codigo text primary key, Descripcion text, Unidades integer, imagen BLOB)";
        String invCompra="CREATE TABLE invcompra(Codigo text primary key, FechaCompra text, PrecioUnidad integer, PrecioVenta integer, PrecioTotal integer)"; //se quito unidades de la tabla de invcompra
        String invVenta="CREATE TABLE invventa(Codigo text, Unidades integer, FechaVenta text, PrecioVenta integer, VentaTotal integer)";
        sqLiteDatabase.execSQL(prodTable);
        sqLiteDatabase.execSQL(invCompra);
        sqLiteDatabase.execSQL(invVenta);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
