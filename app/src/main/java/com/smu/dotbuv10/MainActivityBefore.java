package com.smu.dotbuv10;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smu.dotbuv10.servicioPaciente.Controler.Crear;
import com.smu.dotbuv10.servicioPaciente.Controler.Pacientes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivityBefore extends AppCompatActivity {
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_before);
        preferences =getSharedPreferences("preferences",MODE_PRIVATE);
        if(preferences.getInt("usuario_id",0)>0 && preferences.getString("usuario_nombre",null)!=null  ){
            String url="https://dotbu.online/samuapi/user/generate_auth_cookie/?nonce=+4d080ff7b8&username="+preferences.getString("usuario_nombre",null)+
                    "&password="+preferences.getString("usuario_password",null)+"&insecure=cool";
            validar(url);
        }else{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);
            finish();
        }
    }


    public void irSiguiente(){
            String url2="https://dotbu.online/wp-json/wcra/v1/PacienteUsuario/?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&id_usuario="+preferences.getInt("usuario_id",0);
            validarCant(url2);
    }
    private void validar(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                try{
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);
                    String error=jObj.getString("status");
                    if(error.equals("ok")) {
                        JSONObject user =jObj.getJSONObject("user");
                        String url2="https://dotbu.online/samuapi/user/xprofile/?&user_id="+user.getString("id")+"&field=Tipo de usuario&insecure=cool";
                        validartipo(url2,user);
                    }else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }}
                catch (JSONException e) {
                    Toast.makeText(MainActivityBefore.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivityBefore.this,error.toString(),Toast.LENGTH_LONG).show();
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

    private void validartipo(String URL,JSONObject user){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try{
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);
                    String error=jObj.getString("Tipo de usuario");
                    if(error.equals("Cuidador/a")) {

                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putInt("usuario_id",user.getInt("id"));
                        editor.putString("usuario_nombre",user.getString("username"));
                        editor.commit();
                        irSiguiente();


                    }else {
                        Toast.makeText(MainActivityBefore.this,jObj.getString("error"),Toast.LENGTH_LONG).show();
                    }}
                catch (JSONException e) {
                    Toast.makeText(MainActivityBefore.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivityBefore.this,error.toString(),Toast.LENGTH_LONG).show();
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
    private void validarCant(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try{
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);
                    int error=jObj.getInt("data");
                    if(error>0) {
                        Intent intent = new Intent(getApplicationContext(), Pacientes.class);

                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(getApplicationContext(), Crear.class);

                        startActivity(intent);
                        finish();
                    }}
                catch (JSONException e) {
                    Toast.makeText(MainActivityBefore.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivityBefore.this,error.toString(),Toast.LENGTH_LONG).show();
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
