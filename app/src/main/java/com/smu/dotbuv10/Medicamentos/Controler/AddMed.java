package com.smu.dotbuv10.Medicamentos.Controler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smu.dotbuv10.Alarm.Model.Alarm;
import com.smu.dotbuv10.Medicamentos.Model.NotiWorker;
import com.smu.dotbuv10.R;
import com.smu.dotbuv10.servicioPaciente.Model.Entidad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AddMed extends AppCompatActivity {
    Spinner opciones;
    ArrayAdapter<String> adapter;
    SharedPreferences preferences;
    Button add;
    Button imagen;
    EditText Cantidadias;
    EditText Cantmin;
    EditText hora;
    EditText Stock;
    ProgressBar mUploa;
    String imgULR="";
    ImageView im;
    Boolean crearted=false;
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    Uri imagePhath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences =getSharedPreferences("preferences",MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);
        opciones=findViewById(R.id.sp01);
        imagen=findViewById(R.id.formula);
        im=findViewById(R.id.imageView6);
        mUploa=findViewById(R.id.upload_progress_bar2);
        medicamentos();
        hora=findViewById(R.id.hora);
        Stock=findViewById(R.id.Stock);
        Cantidadias=findViewById(R.id.Cantidadias);
        Cantmin=findViewById(R.id.Cantmin);
        add=findViewById(R.id.addmedicamento);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String ar[]=opciones.getSelectedItem().toString().split(" ");
                medicamentosadd(Integer.parseInt(ar[0]),SystemClock.elapsedRealtime(),Cantidadias.getText(),
                        hora.getText(),Stock.getText(),Cantmin.getText(),imgULR);
                    add.setEnabled(false);

            }

        });
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cargar= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                cargar.setType("image/");
                startActivityForResult(cargar.createChooser(cargar,"seleccione la Aplicacion"),10);

            }
        });

    }
    private void medicamentos(){
        ArrayList<String> listoptions =new ArrayList<>();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://dotbu.online/wp-json/wcra/v1/ListarMedicamento/?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jObj = new JSONObject(response);
                    JSONArray data=jObj.getJSONArray("data");
                    for(int i =0; i< data.length();i++){
                        JSONObject a= (JSONObject)data.get(i);
                        listoptions.add(a.getInt("id")+" / "+a.getString("Producto")+" / "+a.getString("Unidad_de_referencia"));
                        System.out.println(a);
                    }

                    adapter=  new ArrayAdapter<String>(AddMed.this,android.R.layout.simple_spinner_dropdown_item, listoptions);
                    adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                    opciones.setAdapter(adapter);
                }
                catch (JSONException e) {
                    Toast.makeText(AddMed.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddMed.this,error.toString(),Toast.LENGTH_LONG).show();
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
    private void medicamentosadd(int idMed, Long FechaInicio, Editable CantDias, Editable FrecuenciaSuministro, Editable Stock, Editable CantidadMinima, String image){
        Intent intent = getIntent();
        Entidad entidad =(Entidad)intent.getSerializableExtra("entidad");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://dotbu.online/wp-json/wcra/v1/" +
                "addmed/" +
                "?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&id_paciente=" +entidad.getId()+
                "&id_Medicamento=" +idMed+
                "&FechaInicio=" +FechaInicio+
                "&CantDias=" +CantDias+
                "&FrecuenciaSuministro=" +Integer.parseInt(FrecuenciaSuministro.toString())+
                "&Stock=" +Stock+
                "&CantidadMinima=" +CantidadMinima+
                "&image="+image, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Data.Builder data = new Data.Builder();
                data.putString("Tag",idMed+"|"+entidad.getId());
                data.putInt("frecuencia",Integer.parseInt(FrecuenciaSuministro.toString()));
                Toast.makeText(AddMed.this,"MED CREADO creado",Toast.LENGTH_LONG).show();

                OneTimeWorkRequest mywork = new OneTimeWorkRequest.Builder(NotiWorker.class)
                        .setInitialDelay(Integer.parseInt(FrecuenciaSuministro.toString()), TimeUnit.HOURS).addTag(idMed+"|"+entidad.getId()).setInputData(data.build())
                        .build();
                WorkManager.getInstance(AddMed.this).enqueue(mywork);
                    crearted=true;
                    finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddMed.this,error.toString(),Toast.LENGTH_LONG).show();
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
    private void medicamentoselect(){
        Intent intent = getIntent();
        Entidad entidad =(Entidad)intent.getSerializableExtra("entidad");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://dotbu.online/wp-json/wcra/v1/selectmedpac/" +
                "?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&id_paciente="+entidad.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jObj = new JSONObject(response);
                    JSONArray data=jObj.getJSONArray("data");
                }
                catch (JSONException e) {
                    Toast.makeText(AddMed.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddMed.this,error.toString(),Toast.LENGTH_LONG).show();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
            imagePhath=data.getData();

            String path="fireIcon/"+ UUID.randomUUID()+".png";
            StorageReference fireIconRef=storage.getReference(path);
            UploadTask uploadTask= fireIconRef.putFile(imagePhath);
            mUploa.setVisibility(View.VISIBLE);
            uploadTask.addOnCompleteListener(AddMed.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Log.i("MA","upload task complete");
                    mUploa.setVisibility(View.GONE);
                    im.setImageURI(imagePhath);
                    imagen.setEnabled(false);
                }
            });
            Task<Uri> getDownloadUriTask =uploadTask.continueWithTask(
                    new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }

                            return fireIconRef.getDownloadUrl();
                        }
                    }
            );
            getDownloadUriTask.addOnCompleteListener(AddMed.this, new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        System.out.println(downloadUri.toString());
                        imgULR= downloadUri.toString();
                        System.out.println(imgULR);
                    }
                }
            });
        }
    }
}