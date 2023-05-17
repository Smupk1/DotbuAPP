package com.smu.dotbuv10.servicioPaciente.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smu.dotbuv10.servicioPaciente.Controler.Botones;
import com.smu.dotbuv10.servicioPaciente.Model.Entidad;
import com.smu.dotbuv10.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adaptador  extends BaseAdapter {
    private ArrayList<Entidad> listEntidad;
    private Context context;
    private LayoutInflater inflater;

    public Adaptador(Context context, ArrayList<Entidad> listEntidad) {
        this.context = context;
        this.listEntidad = listEntidad;
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
        final Entidad entidad = (Entidad) getItem(position);

        // CREAMOS E INICIALIZAMOS LOS ELEMENTOS DEL ITEM DE LA LISTA
        convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
        ImageView imgFoto = (ImageView) convertView.findViewById(R.id.imgFoto);
        TextView tvTitulo = (TextView) convertView.findViewById(R.id.tvTitulo);
        TextView tvContenido = (TextView) convertView.findViewById(R.id.tvContenido);
        // LLENAMOS LOS ELEMENTOS CON LOS VALORES DE CADA ITEM
        String str=entidad.getImgFoto();
        Picasso.with(context).load(str).into(imgFoto);
        tvTitulo.setText(entidad.getTitulo());
        tvContenido.setText(entidad.getContenido());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Botones.class);
                intent.putExtra("id", entidad);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
