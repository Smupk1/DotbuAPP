package com.smu.dotbuv10.Geofence.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smu.dotbuv10.R;
import com.smu.dotbuv10.databinding.ActivityAddGeoFenceBinding;
import com.smu.dotbuv10.servicioPaciente.Controler.RadiusMapsPojo;
import com.smu.dotbuv10.servicioPaciente.Model.Entidad;

import java.util.Random;

public class AddGeoFence extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityAddGeoFenceBinding binding;
    private GeofencingClient geofencingClient;
    private FusedLocationProviderClient fusedLocationClient;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";
    private Slider rangeSlider;
    private LatLng slatLng;
    String code="N9TT-9G0A-B7FQ-RANC";
    private float values = 2;
    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private static final String TAG = "AddGeoFence";
    private static int UPDATE_INTERVAL = 5000;
    private static int FATEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    DatabaseReference ref;
    GeoFire geoFire;
    private LocationRequest mLocationRequest;
    private static final int MY_PERMISSION_REQUEST_CODE = 7192;
    private static final int PLAY_SERVICE_RESULATION_REQUEST = 300193;
    Button añadir;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocaiton;
    Marker mCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddGeoFenceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapmapGEo);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        rangeSlider = findViewById(R.id.slider);
        ref = FirebaseDatabase.getInstance().getReference("Locations");
        geoFire = new GeoFire(ref);
        añadir=findViewById(R.id.AddGeo);
        Intent intent = getIntent();
        code=(String) intent.getSerializableExtra("code");
        añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFence(slatLng,values);
            }
        });
        rangeSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                values = value;
                System.out.println(value);
                mMap.clear();
                addMarker(slatLng);
                addCircle(slatLng, values);
            }
        });
    }

    private void addFence(LatLng slatLng, float values) {
        DatabaseReference geof = FirebaseDatabase.getInstance().getReference();
        RadiusMapsPojo radiusMapsPojo=new RadiusMapsPojo();
        radiusMapsPojo.setRadio(values);
        radiusMapsPojo.setLatitud(slatLng.latitude);
        radiusMapsPojo.setLongitud(slatLng.longitude);
        geof.child("GeoFence").child(code).setValue(radiusMapsPojo);
        finish();
        Toast.makeText(this, "El geofence ha sido creado con exito", Toast.LENGTH_SHORT).show();
    }

        @Override
        public void onMapReady (GoogleMap googleMap){
            mMap = googleMap;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            }
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 19));
                                    slatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    addMarker(new LatLng(location.getLatitude(), location.getLongitude()));
                                    addCircle(new LatLng(location.getLatitude(), location.getLongitude()), 2);
                                }
                            }
                        });



            mMap.setOnMapLongClickListener(this);

        }


        @Override
        public void onMapLongClick (LatLng latLng){
            System.out.println("click" + slatLng + latLng);
            mMap.clear();
            slatLng = latLng;
            addMarker(slatLng);
            addCircle(slatLng, values);
        }

        private void addMarker (LatLng latLng){
            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
            mMap.addMarker(markerOptions);
        }

        private void addCircle (LatLng latLng,float radius){
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.radius(radius);
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
            circleOptions.fillColor(Color.argb(64, 255, 0, 0));
            circleOptions.strokeWidth(4);
            mMap.addCircle(circleOptions);
        }


    }
