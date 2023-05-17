package com.smu.dotbuv10.Location.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smu.dotbuv10.Location.Model.MapsPojo;
import com.smu.dotbuv10.R;
import com.smu.dotbuv10.databinding.ActivityMapsPacientBinding;
import com.smu.dotbuv10.servicioPaciente.Model.Entidad;

import java.util.ArrayList;

public class MapsActivityPacient extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference databaseReference;
    private ActivityMapsPacientBinding binding;
    private String code="N9TT-9G0A-B7FQ-RANC";
    private ArrayList<Marker> rtMarker=new ArrayList<>();
    private Entidad entidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("/////////////////////////////////////////////////////");
        System.out.println("mapa listo");
        System.out.println("/////////////////////////////////////////////////////");
        binding = ActivityMapsPacientBinding.inflate(getLayoutInflater());
        System.out.println("/////////////////////////////////////////////////////");
        System.out.println("mapa listo");
        System.out.println("/////////////////////////////////////////////////////");
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        entidad =(Entidad)intent.getSerializableExtra("entidad");
        code=(String) intent.getSerializableExtra("code");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        databaseReference.child("Locations/"+code).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(Marker mk:rtMarker){
                        mk.remove();
                    }
                System.out.println(snapshot.toString());
                    MapsPojo mp =snapshot.getValue(MapsPojo.class);
                    Double longitud=mp.getLongitud();
                    Double latitud= mp.getLatitud();
                    MarkerOptions markerOptions=new MarkerOptions();
                    markerOptions.position(new LatLng(latitud,longitud)).title("Paciente: "+entidad.getTitulo());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud,longitud),19));
                    rtMarker.add( mMap.addMarker(markerOptions));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}