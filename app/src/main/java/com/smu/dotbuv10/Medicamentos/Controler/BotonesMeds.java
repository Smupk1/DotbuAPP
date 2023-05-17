package com.smu.dotbuv10.Medicamentos.Controler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.smu.dotbuv10.Medicamentos.Model.NotiWorker;
import com.smu.dotbuv10.R;
import com.smu.dotbuv10.servicioPaciente.Model.Entidad;

import java.util.concurrent.TimeUnit;

public class BotonesMeds extends AppCompatActivity {
Button b1;
Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botones_meds);
        b1=findViewById(R.id.eAlarm);
        b2=findViewById(R.id.aAlarma);
        Intent intent = getIntent();
        Entidad entidad =(Entidad)intent.getSerializableExtra("Entidad");
        int id= (int) intent.getSerializableExtra("id");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance().cancelAllWorkByTag(entidad.getId()+"|"+id);
                Toast.makeText(BotonesMeds.this,"alarma elimianda",Toast.LENGTH_LONG).show();
                finish();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data.Builder data = new Data.Builder();
                data.putString("Tag",entidad.getId()+"|"+id);
                data.putInt("frecuencia",Integer.parseInt(entidad.getContenido()));
                OneTimeWorkRequest mywork = new OneTimeWorkRequest.Builder(NotiWorker.class)
                        .setInitialDelay(Integer.parseInt(entidad.getContenido()), TimeUnit.HOURS).addTag(entidad.getId()+"|"+id).setInputData(data.build())
                        .build();
                Toast.makeText(BotonesMeds.this,"alarma añadida",Toast.LENGTH_LONG).show();
                WorkManager.getInstance(BotonesMeds.this).enqueue(mywork);

                finish();
            }
        });
    }
}