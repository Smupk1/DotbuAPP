package com.smu.dotbuv10.servicioPaciente.Controler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.smu.dotbuv10.Cita.Model.LIstCitas;
import com.smu.dotbuv10.Location.Controller.BotonesMapas2;
import com.smu.dotbuv10.Medicamentos.Controler.Medicamentos;
import com.smu.dotbuv10.R;
import com.smu.dotbuv10.servicioPaciente.Model.Entidad;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Botones extends AppCompatActivity {
    Button bt1;
    Button bt2;
    Button bt3;
    Button location;
    Button bt5;
    SharedPreferences preferences;
    int id=0;
    ImageView imageView4;
    TextView textView;
    Entidad entidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botones);
        imageView4=findViewById(R.id.imageView4);
        bt1=findViewById(R.id.cita);
        bt2=findViewById(R.id.medicamento);
        bt5=findViewById(R.id.modify);
        location=findViewById(R.id.location);
        textView=findViewById(R.id.NomP);
        preferences =getSharedPreferences("preferences",MODE_PRIVATE);
        Intent intent = getIntent();
        Entidad entidad =(Entidad)intent.getSerializableExtra("id");
        id=entidad.getId();
        paciente();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LIstCitas.class);
                intent.putExtra("entidad", entidad);
                startActivity(intent);
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditarP.class);
                intent.putExtra("entidad", entidad);
                startActivity(intent);
            }

        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Medicamentos.class);
                intent.putExtra("entidad", entidad);
                startActivity(intent);
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CodigoUbicacion();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        paciente();

    }
    public void medicina(){
        Intent intent = new Intent(getApplicationContext(),Medicamentos.class);
        //intent.putExtra();
        startActivity(intent);
    }
    private void paciente(){
        ArrayList<Entidad> listEntidad =new ArrayList<>();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://dotbu.online/wp-json/wcra/v1/selectPaciente/?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&id="+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jObj = new JSONObject(response);
                    JSONObject data=jObj.getJSONObject("data");

                        JSONObject a= data;
                        entidad=new Entidad(a.getInt("id"),a.getString("Avatar"),a.getString("Nombre"), a.getString("Fecha"));


                    textView.setText(entidad.getTitulo());
                    Picasso.with(Botones.this).load(entidad.getImgFoto()).into(imageView4);
                    paciente2();
                }
                catch (JSONException e) {
                    Toast.makeText(Botones.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Botones.this,error.toString(),Toast.LENGTH_LONG).show();
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
    }
    private void paciente2(){
        ArrayList<Entidad> listEntidad =new ArrayList<>();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://dotbu.online/wp-json/wcra/v1/selectpermiso/?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&id_usuario="+preferences.getInt("usuario_id",0)+"&id="+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jObj = new JSONObject(response);
                    JSONObject data=jObj.getJSONObject("data");
                    JSONObject a= data;
                    if(!a.getString("Rol").equals("A")){
                        bt5.setEnabled(false);
                    }
                }
                catch (JSONException e) {
                    Toast.makeText(Botones.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Botones.this,error.toString(),Toast.LENGTH_LONG).show();
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
    }
    private void CodigoUbicacion(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://dotbu.online/wp-json/wcra/v1/selectcodPac/?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws"+"&id_paciente="+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jObj = new JSONObject(response);
                    JSONArray data=jObj.getJSONArray("data");
                    if(data.length()==0){
                        final String[] m_Text = {""};
                        AlertDialog.Builder builder = new AlertDialog.Builder(Botones.this);
                        builder.setTitle("Title");

// Set up the input
                        final EditText input = new EditText(Botones.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);

// Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m_Text[0] = input.getText().toString();
                                System.out.println("holu");
                                StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://dotbu.online/wp-json/wcra/v1/selectcodPacExist/?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&codigo="+m_Text[0], new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println("holu");
                                        Boolean flag=false;
                                        try{
                                            JSONObject jObj = new JSONObject(response);
                                            JSONArray data=jObj.getJSONArray("data");
                                            System.out.println(data.length());
                                            for(int i =0; i< data.length();i++){
                                                JSONObject a= (JSONObject)data.get(i);
                                                if(a.getString("test").equals("Existe")){
                                                    Toast.makeText(Botones.this,"El Codigo existe no puede ser añadido",Toast.LENGTH_LONG).show();
                                                    i=data.length();
                                                    flag=true;
                                                }
                                            }
                                            if(flag){
                                                System.out.println("medfgdf");
                                            }else{
                                                System.out.println("entre");
                                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                                Query query = databaseReference.child("Locations");
                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            for (DataSnapshot citi : snapshot.getChildren()) {
                                                                System.out.println(citi.getKey().toString());
                                                                if (citi.getKey().toString().equals(m_Text[0])){

                                                                    StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://dotbu.online/wp-json/wcra/v1/insertcodPac/?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&codigo="+m_Text[0]+"&id_paciente="+id, new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            Toast.makeText(Botones.this,response,Toast.LENGTH_LONG).show();
                                                                        }

                                                                    }, new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {
                                                                            Toast.makeText(Botones.this,error.toString(),Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }){
                                                                        @Nullable
                                                                        @Override
                                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                                            Map<String,String> parametros=new HashMap<String,String>();
                                                                            return parametros;
                                                                        }
                                                                    };
                                                                    RequestQueue requestQueue   = Volley.newRequestQueue(Botones.this);
                                                                    requestQueue.add(stringRequest);

                                                                }else{
                                                                    System.out.println("nohay");
                                                                }
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }
                                        catch (JSONException e) {
                                            Toast.makeText(Botones.this,e.toString(),Toast.LENGTH_LONG).show();
                                        }
                                    }

                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(Botones.this,error.toString(),Toast.LENGTH_LONG).show();
                                    }
                                }){
                                    @Nullable
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String> parametros=new HashMap<String,String>();
                                        return parametros;
                                    }
                                };
                                RequestQueue requestQueue   = Volley.newRequestQueue(Botones.this);
                                requestQueue.add(stringRequest);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();

                    }else{
                        JSONObject jObj2 =(JSONObject)data.get(0);
                        String code=jObj2.getString("codigo");
                        Intent intent = new Intent(getApplicationContext(), BotonesMapas2.class);
                        intent.putExtra("entidad", entidad);
                        intent.putExtra("code", code);
                        startActivity(intent);
                    }
                }
                catch (JSONException e) {
                    Toast.makeText(Botones.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Botones.this,error.toString(),Toast.LENGTH_LONG).show();
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
    }

}