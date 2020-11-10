package com.example.bussinesversion10;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.sqlite.db.SimpleSQLiteQuery;

public class Adaptadorr extends BaseAdapter {

    Context contexto;
    List<producto> listaproductos;

    public Adaptadorr(Context contexto, List<producto> listaproductos) {
        this.contexto = contexto;
        this.listaproductos = listaproductos;
    }

    @Override
    public int getCount() {
        return listaproductos.size();
    }

    @Override
    public Object getItem(int i) {
        return listaproductos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = view;

        LayoutInflater inflate = LayoutInflater.from(contexto);
        vista = inflate.inflate(R.layout.itemlistview,null);

        ImageView imagen = (ImageView)vista.findViewById(R.id.imageView);
        TextView Codigo = (TextView)vista.findViewById(R.id.txt_coodiigoo);
        TextView Desc = (TextView)vista.findViewById(R.id.txt_deesc);
        TextView Unidades = (TextView)vista.findViewById(R.id.txt_uniid);

        Codigo.setText(listaproductos.get(i).getCod().toString());
        Desc.setText(listaproductos.get(i).getDesc().toString());
        Unidades.setText(listaproductos.get(i).getUnidades().toString());
        imagen.setImageBitmap(listaproductos.get(i).getImagen());

        return vista;
    }
}
