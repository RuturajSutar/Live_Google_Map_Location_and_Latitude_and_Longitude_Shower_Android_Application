package com.pirates.mygooglemapapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity   {

    private GoogleMap mMap,mmMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);




        if (Build.VERSION.SDK_INT >= 23){
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                //Request Location Again
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else {
                //Request Location Permission
                startService();
            }
        }
        else{
            //Start the Location service
            startService();
        }



    }


    void startService(){
        LocationBroadcastReciever locationBroadcastReciever=new LocationBroadcastReciever();
        IntentFilter intentFilter=new IntentFilter("ACT_LOC");
        registerReceiver(locationBroadcastReciever,intentFilter);
        Intent intent = new Intent(MapsActivity.this,LocationService.class);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startService();
                }
                else {
                    Toast.makeText(this, "Give me Permission", Toast.LENGTH_LONG).show();
                }
        }
    }

    public class LocationBroadcastReciever extends BroadcastReceiver implements OnMapReadyCallback{

        GoogleMap map;
        public  double lat,longitude;
        SupportMapFragment mapFragment;
        @Override
        public void onReceive(Context context, Intent intent) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            if (intent.getAction().equals("ACT_LOC")){

                lat=intent.getDoubleExtra("latitude",0f);
                longitude=intent.getDoubleExtra("longitude",0f);
                Toast.makeText(context, "Latitude is: "+lat+" and Longitude is: "+longitude, Toast.LENGTH_SHORT).show();

            }
        }



        @Override

        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            LatLng myposition = new LatLng(lat, longitude);
            // Toast.makeText(MapsActivity.this, ""+new MarkerOptions().position(myposition).title("This is my Position"), Toast.LENGTH_SHORT).show();
            //Toast.makeText(MapsActivity.this, lat + "           " + longitude, Toast.LENGTH_SHORT).show();
            map.addMarker(new MarkerOptions().position(myposition).title("This is my Position"));
            map.moveCamera(CameraUpdateFactory.newLatLng(myposition));
        }
    }



}
