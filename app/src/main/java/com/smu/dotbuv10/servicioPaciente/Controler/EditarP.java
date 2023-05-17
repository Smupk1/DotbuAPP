package com.smu.dotbuv10.servicioPaciente.Controler;

import android.app.AlertDialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.smu.dotbuv10.servicioPaciente.Model.Entidad;
import com.smu.dotbuv10.User.Controller.usuariosA;
import com.smu.dotbuv10.User.Controller.usuariosD;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditarP extends AppCompatActivity {
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
    Button añadir;
    Button eliminar;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_p);
        initDatePicker();
        im=findViewById(R.id.editimageView5);
        imagen=findViewById(R.id.editImage);
        mUploa=findViewById(R.id.upload_progress_bar);
        dateButton = findViewById(R.id.editFecha);
        preferences =getSharedPreferences("preferences",MODE_PRIVATE);
        crear=findViewById(R.id.editbutton3);
        editTextTextPersonName=findViewById(R.id.editPersonName);
        dateButton.setText(getTodaysDate());
        añadir=findViewById(R.id.añadirUsers);
        eliminar=findViewById(R.id.eliminarUsers);
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
        añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Entidad entidad =(Entidad)intent.getSerializableExtra("entidad");
                Intent intent2 = new Intent(getApplicationContext(), usuariosA.class);
                intent2.putExtra("entidad",entidad);
                startActivity(intent2);
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Entidad entidad =(Entidad)intent.getSerializableExtra("entidad");
                Intent intent2 = new Intent(getApplicationContext(), usuariosD.class);
                intent2.putExtra("entidad",entidad);
                startActivity(intent2);
            }
        });
    }

    private void crear(){
        Intent intent = getIntent();
        Entidad entidad =(Entidad)intent.getSerializableExtra("entidad");
        String a="No se han realizado cambios";
        if (!editTextTextPersonName.getText().toString().equals("")){
                entidad.setTitulo(editTextTextPersonName.getText().toString());
            a="paciente editado";
            }
        if (!dateButton.getText().toString().equals(getTodaysDate())){
            String date=dateButton.getText().toString();
            String[] datea=date.split("-");
            String dateReal=datea[2]+"-"+datea[1]+"-"+datea[0];
            entidad.setContenido(dateReal);
            a="paciente editado";
        }
        if (!imagen.isEnabled()){
            entidad.setImgFoto(imgULR);
            a="paciente editado";
        }
        editp(entidad);
        Toast.makeText(this,a,Toast.LENGTH_LONG).show();
    }

    private void editp(Entidad entidad){
        String[] arrOfStr = entidad.getImgFoto().split("%", 2);
        entidad.setImgFoto(arrOfStr[0]+"&F="+arrOfStr[1]);
        String URL="https://dotbu.online/wp-json/wcra/v1/editPac/?" +
                "secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&"+
                "Nombre=" +entidad.getTitulo()+
                "&Fecha=" +entidad.getContenido()+
                "&id=" +entidad.getId()+
                "&Avatar=" +entidad.getImgFoto();
        System.out.println(URL);
        Log.e("edit",URL);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("edit",response);
                finish();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.toString());
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
            uploadTask.addOnCompleteListener(EditarP.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
            getDownloadUriTask.addOnCompleteListener(EditarP.this, new OnCompleteListener<Uri>() {
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
        fecha=year+"-"+month+"-"+day;
        return  day + "-"+ month  + "-" + year;
    }


    public void openDatePicker(View view)
    {

        datePickerDialog.show();
    }

}