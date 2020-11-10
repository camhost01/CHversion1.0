package com.example.bussinesversion10;

import android.graphics.Bitmap;

public class producto {
    private String cod;
    private String desc;
    private String unidades;
    private Bitmap imagen;

    public producto(String cod, String desc, String unidades, Bitmap imagen) {
        this.cod = cod;
        this.desc = desc;
        this.unidades = unidades;
        this.imagen = imagen;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
