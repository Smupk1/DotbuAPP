package com.smu.dotbuv10.Cita.Controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smu.dotbuv10.Cita.Model.NotiWorker2;
import com.smu.dotbuv10.Medicamentos.Controler.AddMed;
import com.smu.dotbuv10.Medicamentos.Model.NotiWorker;
import com.smu.dotbuv10.R;
import com.smu.dotbuv10.servicioPaciente.Model.Entidad;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AddCita extends AppCompatActivity {
    Button bt1,bt2,add;
    int hour,minute;
    EditText lugar,medico;
    String fecha="";
    SharedPreferences preferences;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cita);
        bt1=findViewById(R.id.HoraCita);
        bt2=findViewById(R.id.FechaCita);
        add=findViewById(R.id.AñadorCita);
        lugar=findViewById(R.id.Lugar);
        medico=findViewById(R.id.Medico);
        preferences =getSharedPreferences("preferences",MODE_PRIVATE);
        initDatePicker();
        bt2.setText(getTodaysDate());
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePiker();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(view);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String horaFecha =fecha+" " +bt1.getText().toString();
                Date date2;
                SimpleDateFormat dateFormatter
                            = new SimpleDateFormat("yyyy-M-d HH:mm");
                    Date date= null;
                    try {
                        date = (Date) dateFormatter.parse(horaFecha);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    date2=date;

                citasadd(horaFecha,date2,
                        lugar.getText(),medico.getText());
                add.setEnabled(false);
            }
        });
    }
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                bt2.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
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

    private void popTimePiker(){
        TimePickerDialog.OnTimeSetListener onTimeSetListener= new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hour=i;
                minute=i1;
                bt1.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));
            }
        };
        TimePickerDialog timePickerDialog=new TimePickerDialog(this,onTimeSetListener,hour,minute,true);
        timePickerDialog.setTitle("Hora de la cita");
        timePickerDialog.show();
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


    private void citasadd(String Fecha,Date date2,Editable Lugar, Editable Medico){
        Intent intent = getIntent();
        Entidad entidad =(Entidad)intent.getSerializableExtra("entidad");
        System.out.println(
                Fecha
        );
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://dotbu.online/wp-json/wcra/v1/" +
                "insertCita/" +
                "?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&id_paciente=" +entidad.getId()+
                "&id_usuario=" +preferences.getInt("usuario_id",0)+
                "&FechaCita=" +Fecha+
                "&Lugar=" +Lugar.toString()+
                "&Medico="+Medico.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Data.Builder data = new Data.Builder();
                data.putString("Tag", entidad.getTitulo());
                data.putString("key", Lugar.toString());
                data.putString("key2", Fecha);
                        Long date1= System.currentTimeMillis();
                        int h =hoursDifference(date2,date1);
                System.out.println(h);
                if(h<=0){
                    h=1;

                }
                if(h>4){
                    h=h-3;

                }

                OneTimeWorkRequest mywork = new OneTimeWorkRequest.Builder(NotiWorker2.class)
                        .setInitialDelay(h, TimeUnit.HOURS).setInputData(data.build())
                        .build();
                WorkManager.getInstance(AddCita.this).enqueue(mywork);
                        finish();




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddCita.this,error.toString(),Toast.LENGTH_LONG).show();
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

    private static int hoursDifference(Date date1, Long date2) {

        final int MILLI_TO_HOUR = 1000 * 60 * 60;
        return (int) (date1.getTime() - date2) / MILLI_TO_HOUR;
    }
}