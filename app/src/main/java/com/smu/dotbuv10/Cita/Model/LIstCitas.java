package com.smu.dotbuv10.Cita.Model;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.smu.dotbuv10.Cita.Adaptor.AdaptadorCita;
import com.smu.dotbuv10.Cita.Controller.AddCita;
import com.smu.dotbuv10.Medicamentos.Adaptor.AdaptadorMeds;
import com.smu.dotbuv10.Medicamentos.Controler.Medicamentos;
import com.smu.dotbuv10.R;
import com.smu.dotbuv10.servicioPaciente.Model.Entidad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LIstCitas extends AppCompatActivity {
Button button;
    Entidad entidad;
    AdaptadorCita adaptador;
    private ListView lvItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_citas);
        button=findViewById(R.id.addCita);
        Intent intent = getIntent();
        lvItems=findViewById(R.id.lvICitas);
        entidad =(Entidad)intent.getSerializableExtra("entidad");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddCita.class);
                intent.putExtra("entidad", entidad);
                startActivity(intent);
            }
        });
        MedList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MedList();
    }

    private void MedList(){
        ArrayList<EntidadCita> listEntidad =new ArrayList<>();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://dotbu.tech/wp-json/wcra/v1/sleectCitas/?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&id_paciente="+entidad.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jObj = new JSONObject(response);
                    JSONArray data=jObj.getJSONArray("data");
                    for(int i =0; i< data.length();i++){
                        JSONObject a= (JSONObject)data.get(i);
                        listEntidad.add(new EntidadCita(a.getInt("id_usuario"),entidad.getTitulo(),a.getString("Fechacita"),a.getString("Lugar"),a.getString("Medico")));

                    }
                    adaptador = new AdaptadorCita(listEntidad,LIstCitas.this,entidad.getId());
                    lvItems.setAdapter(adaptador);

                }
                catch (JSONException e) {
                    Toast.makeText(LIstCitas.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LIstCitas.this,"no hay citas por listar",Toast.LENGTH_LONG).show();
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