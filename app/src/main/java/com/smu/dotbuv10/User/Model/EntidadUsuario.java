package com.smu.dotbuv10.User.Model;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EntidadUsuario implements Serializable {
    private int id;
    private String imgFoto;
    private String titulo;
    private String contenido;
    Context context;

    public EntidadUsuario(int id, String titulo,String imgFoto, String contenido) {
        this.id=id;
        this.imgFoto=imgFoto;
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
