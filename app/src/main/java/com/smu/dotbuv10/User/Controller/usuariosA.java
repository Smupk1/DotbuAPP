package com.smu.dotbuv10.User.Controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smu.dotbuv10.R;
import com.smu.dotbuv10.User.Adaptor.AdaptadorUsuario;
import com.smu.dotbuv10.User.Model.EntidadUsuario;
import com.smu.dotbuv10.servicioPaciente.Model.Entidad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class usuariosA extends AppCompatActivity {
    SharedPreferences preferences;
    Button añadir;
    private ListView lvItems;
    private AdaptadorUsuario adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_user);
        preferences =getSharedPreferences("preferences",MODE_PRIVATE);
        lvItems = (ListView) findViewById(R.id.lvIUsuariosa);
        usuariosad();
    }

    @Override
    public void onResume(){
        super.onResume();
        usuariosad();

    }

    private void usuariosad(){
        ArrayList<EntidadUsuario> listEntidad =new ArrayList<>();
        Intent intent = getIntent();
        Entidad entidad =(Entidad)intent.getSerializableExtra("entidad");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://dotbu.online/wp-json/wcra/v1/listarUserNoPac/?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&id_paciente="+entidad.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    System.out.println("Hola"+response);
                    JSONObject jObj = new JSONObject(response);
                    JSONArray data=jObj.getJSONArray("data");
                    for(int i =0; i< data.length();i++){
                        JSONObject a= (JSONObject)data.get(i);
                        listEntidad.add(new EntidadUsuario(a.getInt("id"),a.getString("display_name"),a.getString("avatar"),null));

                    }
                    adaptador = new AdaptadorUsuario(usuariosA.this, listEntidad,entidad.getId(),0);
                    lvItems.setAdapter(adaptador);

                }
                catch (JSONException e) {
                    Toast.makeText(usuariosA.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(usuariosA.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                return parametros;
            }
        };
        RequestQueue requestQueue   = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }



}