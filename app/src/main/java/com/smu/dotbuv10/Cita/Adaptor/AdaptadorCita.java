package com.smu.dotbuv10.Cita.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smu.dotbuv10.Cita.Model.EntidadCita;
import com.smu.dotbuv10.Medicamentos.Controler.BotonesMeds;
import com.smu.dotbuv10.R;
import com.smu.dotbuv10.servicioPaciente.Model.Entidad;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorCita  extends BaseAdapter {

    private ArrayList<EntidadCita> listEntidad;
    private Context context;
    private LayoutInflater inflater;
    int id;

    public AdaptadorCita(ArrayList<EntidadCita> listEntidad, Context context, int id) {
        this.listEntidad = listEntidad;
        this.context = context;
        this.id = id;
    }

    @Override
    public int getCount() {
        return listEntidad.size();
    }

    @Override
    public Object getItem(int position) {
        return listEntidad.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // OBTENER EL OBJETO POR CADA ITEM A MOSTRAR
        final EntidadCita entidad2 = (EntidadCita) getItem(position);

        // CREAMOS E INICIALIZAMOS LOS ELEMENTOS DEL ITEM DE LA LISTA
        convertView = LayoutInflater.from(context).inflate(R.layout.item2, null);
        TextView tvHospital = (TextView) convertView.findViewById(R.id.tvHospital);
        TextView tvDoctor = (TextView) convertView.findViewById(R.id.tvDoctor);
        TextView TVPaciente = (TextView) convertView.findViewById(R.id.TVPaciente);
        TextView tvFecha = (TextView) convertView.findViewById(R.id.tvFecha);
        // LLENAMOS LOS ELEMENTOS CON LOS VALORES DE CADA ITEM
        tvHospital.setText(entidad2.getContenido());
        tvDoctor.setText(entidad2.getMedico());
        TVPaciente.setText(entidad2.getImgFoto());
        tvFecha.setText(entidad2.getTitulo());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        return convertView;
    }
}
