package com.smu.dotbuv10.Location.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.smu.dotbuv10.Geofence.Activity.AddGeoFence;
import com.smu.dotbuv10.Geofence.Activity.Geofence;
import com.smu.dotbuv10.Location.Activity.MapsActivityPacient;
import com.smu.dotbuv10.R;
import com.smu.dotbuv10.servicioPaciente.Controler.Pacientes;
import com.smu.dotbuv10.servicioPaciente.Model.Entidad;

public class BotonesMapas2 extends AppCompatActivity {
    Button location;
    Button geofences;
    Entidad entidad;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_botones_mapas2);
        Intent intent = getIntent();
        entidad =(Entidad)intent.getSerializableExtra("entidad");
        code=(String) intent.getSerializableExtra("code");
        location=findViewById(R.id.button4);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MapsActivityPacient.class);
                intent.putExtra("entidad", entidad);
                intent.putExtra("code", code);
                startActivity(intent );
            }
        });
        geofences=findViewById(R.id.Geofencesir);
        geofences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), AddGeoFence.class);
                intent.putExtra("entidad", entidad);
                intent.putExtra("code", code);
                startActivity(intent );
            }
        });
    }
}