package com.smu.dotbuv10.servicioPaciente.Model;

import android.text.Editable;

import java.io.Serializable;

public class Entidad implements Serializable {
    private int id;
    private String imgFoto;
    private String titulo;
    private String contenido;

    public Entidad(int id,String imgFoto, String titulo, String contenido) {
        this.id=id;
        this.imgFoto = imgFoto;
        this.titulo = titulo;
        this.contenido = contenido;
    }
    public int getId() {
        return id;
    }
    public String getImgFoto() {
        return imgFoto;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public  void setId(int id){
        this.id=id;
    }

    public void setImgFoto(String imgFoto) {
        this.imgFoto = imgFoto;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
