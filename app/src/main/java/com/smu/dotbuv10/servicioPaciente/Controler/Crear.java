package com.smu.dotbuv10.servicioPaciente.Controler;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import android.app.AlertDialog;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smu.dotbuv10.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Crear extends AppCompatActivity {
    SharedPreferences preferences;
    Button imagen;
    ProgressBar mUploa;
    ImageView im;
    String fecha="";
    String imgULR="";
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    Uri imagePhath;
    Button crear;
    EditText editTextTextPersonName;
    EditText editTextTextPersonName2;

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);
        initDatePicker();
        im=findViewById(R.id.imageView5);
        imagen=findViewById(R.id.addImage);
        mUploa=findViewById(R.id.upload_progress_bar);
        dateButton = findViewById(R.id.button2);
        preferences =getSharedPreferences("preferences",MODE_PRIVATE);
        crear=findViewById(R.id.button3);
        editTextTextPersonName=findViewById(R.id.editTextTextPersonName);
        editTextTextPersonName2=findViewById(R.id.editTextTextPersonName2);
        dateButton.setText(getTodaysDate());
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent cargar= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    cargar.setType("image/");
                    startActivityForResult(cargar.createChooser(cargar,"seleccione la Aplicacion"),10);

            }
        });
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(view);
            }
        });
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crear();
            }
        });
    }

    private void crear(){
        String a="";
        if (!editTextTextPersonName.getText().toString().equals("")){
            if (!editTextTextPersonName2.getText().toString().equals("") ){
                if (!dateButton.getText().equals(getTodaysDate())){
                    if (!imagen.isEnabled()){
                            crearp();
                    }else{
                        a="Agregar imagen del paciente";
                    }
                }else{
                    a="Agregar fecha de nacimiento del paciente";
                }
            }else{
                a="Agregar cedula del paciente";
            }
        }else{
            a="Agregar Nombre del paciente";
        }
        Toast.makeText(this,a,Toast.LENGTH_LONG).show();
    }

    private void crearp(){
        String[] arrOfStr = imgULR.split("%", 2);
        imgULR=arrOfStr[0]+"&F="+arrOfStr[1];
        String URL="https://dotbu.online/wp-json/wcra/v1/AddPacienteUsaurio/?" +
                "secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&" +
                "Nombre=" +editTextTextPersonName.getText()+
                "&Cedula=" +editTextTextPersonName2.getText()+
                "&Fecha=" +dateButton.getText()+
                "&usuario=" +preferences.getInt("usuario_id",0)+
                "&Avatar=" +imgULR;
        Log.e("rewquest",URL);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Crear.this,"Paciente creado",Toast.LENGTH_LONG).show();
                finish();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Crear.this,error.toString(),Toast.LENGTH_LONG).show();
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
    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
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
            imagen.setEnabled(false);
            uploadTask.addOnCompleteListener(Crear.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Log.i("MA","upload task complete");
                    mUploa.setVisibility(View.GONE);
                    im.setImageURI(imagePhath);
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
            getDownloadUriTask.addOnCompleteListener(Crear.this, new OnCompleteListener<Uri>() {
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
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
        this.fecha=year+"-"+month+"-"+day;
        return  this.fecha;
    }


    public void openDatePicker(View view)
    {

        datePickerDialog.show();
    }

}