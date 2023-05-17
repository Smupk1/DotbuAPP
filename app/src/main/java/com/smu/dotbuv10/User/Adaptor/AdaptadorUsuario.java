package com.smu.dotbuv10.User.Adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smu.dotbuv10.R;
import com.smu.dotbuv10.User.Model.EntidadUsuario;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptadorUsuario extends BaseAdapter {
    private ArrayList<EntidadUsuario> listEntidad;
    private Context context;
    private LayoutInflater inflater;
    int preferences;
    int t;
    public AdaptadorUsuario(Context context, ArrayList<EntidadUsuario> listEntidad, int id,int t) {
        this.context = context;
        this.listEntidad = listEntidad;
        this.preferences=id;
        this.t=t;
    }

    @Override
    public int getCount() {
        return listEntidad.size();
    }

    @Override
    public Object getItem(int position) {
        return listEntidad.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // OBTENER EL OBJETO POR CADA ITEM A MOSTRAR
        final EntidadUsuario entidad = (EntidadUsuario) getItem(position);

        // CREAMOS E INICIALIZAMOS LOS ELEMENTOS DEL ITEM DE LA LISTA
        convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
        ImageView imgFoto = (ImageView) convertView.findViewById(R.id.imgFoto);
        TextView tvTitulo = (TextView) convertView.findViewById(R.id.tvTitulo);
        TextView tvContenido = (TextView) convertView.findViewById(R.id.tvContenido);
        // LLENAMOS LOS ELEMENTOS CON LOS VALORES DE CADA ITEM
        String str=entidad.getImgFoto();
        if(!str.contains("https://")){
            str="https://"+str;
        }
        System.out.println("hola este es el str"+str);
        Picasso.with(context).load(str).into(imgFoto);
        tvTitulo.setText(entidad.getTitulo());
        tvContenido.setText(entidad.getContenido());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(t==0){
                    añadirCuidador( entidad);
                }else{
                    borrarCuidador( entidad);
                }

                ((Activity)context).finish();
            }
        });

        return convertView;
    }

    private void añadirCuidador(EntidadUsuario entidad){
        System.out.println("https://dotbu.online/wp-json/wcra/v1/addCuidador/?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&usuario="+entidad.getId()+"&id_paciente="+this.preferences);
        StringRequest stringRequest=new StringRequest(
                Request.Method.POST,
                "https://dotbu.online/wp-json/wcra/v1/addCuidador/?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&usuario="+entidad.getId()+"&id_paciente="+this.preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    Toast.makeText(context,"Usuario añadido como cuidador",Toast.LENGTH_LONG).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                return parametros;
            }
        };
        RequestQueue requestQueue   = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }
    private void borrarCuidador(EntidadUsuario entidad){
        System.out.println("https://dotbu.online/wp-json/wcra/v1/deleteCuidador/?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&usuario="+entidad.getId()+"&id_paciente="+this.preferences);
        StringRequest stringRequest=new StringRequest(
                Request.Method.POST,
                "https://dotbu.online/wp-json/wcra/v1/deleteCuidador/?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&usuario="+entidad.getId()+"&id_paciente="+this.preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"Usuario eliminado como cuidador",Toast.LENGTH_LONG).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                return parametros;
            }
        };
        RequestQueue requestQueue   = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }

}
