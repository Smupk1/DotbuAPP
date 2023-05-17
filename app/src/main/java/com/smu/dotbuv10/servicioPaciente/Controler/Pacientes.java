package com.smu.dotbuv10.servicioPaciente.Controler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smu.dotbuv10.Geofence.Activity.AddGeoFence;
import com.smu.dotbuv10.Location.Model.MapsPojo;
import com.smu.dotbuv10.servicioPaciente.Model.Entidad;
import com.smu.dotbuv10.MainActivity;
import com.smu.dotbuv10.R;
import com.smu.dotbuv10.servicioPaciente.Adaptador.Adaptador;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Pacientes extends AppCompatActivity {
    SharedPreferences preferences;
    Button sesion;
    Button nuevo;
    private ListView lvItems;
    private Adaptador adaptador;
    private NotificationManagerCompat notificationCompat;
    Notification notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacientes);
        sesion=findViewById(R.id.sesion);
        nuevo=findViewById(R.id.crearP);
        preferences =getSharedPreferences("preferences",MODE_PRIVATE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel("myCh","My Channel",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        notificationCompat= NotificationManagerCompat.from(this);


        sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CerrarSesion();
            }
        });
        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Crear.class);
                startActivity(intent);
            }
        });
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(null);
        pacientes();
    }
    private void CerrarSesion(){
        preferences.edit().clear().apply();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    @Override
    public void onResume(){
        super.onResume();
        pacientes();

    }

    private void pacientes(){
        ArrayList<Entidad> listEntidad =new ArrayList<>();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, "https://dotbu.online/wp-json/wcra/v1/ListarPacientes/?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&id_usuario="+preferences.getInt("usuario_id",0), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Log.w("Hola",response);
                    JSONObject jObj = new JSONObject(response);
                    JSONArray data=jObj.getJSONArray("data");
                    for(int i =0; i< data.length();i++){
                        JSONObject a= (JSONObject)data.get(i);
                        listEntidad.add(new Entidad(a.getInt("id"),a.getString("Avatar"),a.getString("Nombre"), a.getString("Fecha")));
                        Log.e("error de nombre: ",a.getString("Nombre"));

                        StringRequest stringRequest=new StringRequest(Request.Method.GET, "https://dotbu.online/wp-json/wcra/v1/selectcodPac/?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws"+"&id_paciente="+a.getInt("id"), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    Log.e("Hola",response);
                                    JSONObject jObj = new JSONObject(response);
                                    JSONArray data=jObj.getJSONArray("data");
                                    JSONObject jObj2 =(JSONObject)data.get(0);
                                    String code=jObj2.getString("codigo");

                                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Locations/"+code);
                                    dbref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            System.out.println("el codifgo es"+code);
                                            DatabaseReference geof = FirebaseDatabase.getInstance().getReference();
                                            MapsPojo cords2 = snapshot.getValue(MapsPojo.class);

                                            Query query = geof.child("GeoFence");
                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        for (DataSnapshot citi : snapshot.getChildren()) {
                                                            if(citi.getKey().toString().equals(code)){
                                                                RadiusMapsPojo cords =citi.getValue(RadiusMapsPojo.class);
                                                                System.out.println("cambio");
                                                                Location selected_location = new Location("locationA");
                                                                selected_location.setLatitude(cords.getLatitud());
                                                                selected_location.setLongitude(cords.getLongitud());
                                                                Location near_locations = new Location("locationB");
                                                                near_locations.setLatitude(cords2.getLatitud());
                                                                near_locations.setLongitude(cords2.getLongitud());
                                                                double c = selected_location.distanceTo(near_locations);
                                                                System.out.println("value: "+c+","+cords.getRadio());
                                                                if(c<=cords.getRadio()){
                                                                    System.out.println(cords.getRadio());
                                                                    System.out.println(c);
                                                                }else{
                                                                    sendNotification("atencion paciente fuera de la geofence!!","El paciente ha salido del area de proteccion");
                                                                }

                                                            }

                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }

                                    });

                                }
                                catch (JSONException e) {

                                }
                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Pacientes.this,error.toString(),Toast.LENGTH_LONG).show();
                                Log.e("rewquest2",error.toString());
                            }
                        }){
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> parametros=new HashMap<String,String>();
                                return parametros;
                            }
                        };
                        RequestQueue requestQueue   = Volley.newRequestQueue(Pacientes.this);
                        requestQueue.add(stringRequest);
                        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                            @Override
                            public void onRequestFinished(Request<Object> request) {
                                requestQueue.getCache().clear();
                            }
                        });
                    }

                    adaptador = new Adaptador(Pacientes.this, listEntidad);
                    lvItems.setAdapter(adaptador);

                }
                catch (JSONException e) {
                    Toast.makeText(Pacientes.this,e.toString(),Toast.LENGTH_LONG).show();
                    Log.e("rewquest3",e.toString());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Pacientes.this,error.toString(),Toast.LENGTH_LONG).show();
                Log.e("rewquest4",error.toString());
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
    private void sendNotification(String title, String content) {
        long[] vibrate = { 0, 100, 200, 300 };

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"myCh").setSmallIcon(android.R.drawable.stat_notify_error).setContentTitle(title).setContentText(content).setVibrate(vibrate);
        notification=builder.build();
        notificationCompat.notify(1,notification);

    }
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2, String unit)
    {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "M") {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg)
    {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad)
    {
        return (rad * 180.0 / Math.PI);
    }
}