package com.example.mobile_rest;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;

    Rest_Data marcaRest= new Rest_Data();

    boolean doubleClick=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        // Si funciona la conexión
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {


            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getApplicationContext(), 10);
        }

        //miUbicacion();











    }

// geolocalización por el boton
    public void miUbicacion(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Si hay permisos", Toast.LENGTH_LONG).show();
            mMap.setMyLocationEnabled(true);

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);

            }

        }


    }


    // permitir la geolocalizacion verifica permisos


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {




                        mMap.setMyLocationEnabled(true);

                        Toast.makeText(getApplicationContext(), "permisos de granted", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(getApplicationContext(), "requiere permisos de granted", Toast.LENGTH_LONG).show();
                    //finish();

                }
                break;

        }




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

        double lat;
        double lon;
        mMap = googleMap;

        // ubicación de la sony
        lat= 9.8580378;//mMap.getMyLocation().getLatitude();
        lon= -83.9165151 ;//mMap.getMyLocation().getLongitude();
        //tipo de mapa con caracteristicas
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setBuildingsEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setTrafficEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        Toast.makeText(getApplicationContext(), "onmapredy", Toast.LENGTH_LONG).show();
        //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.builder().target));

        // mo
        marcaRest.latitude=9.86034;
        marcaRest.longitude=-83.76850;
        marcaRest.name="Pizza";

        MostarMarcador(googleMap,marcaRest.name,marcaRest.latitude,marcaRest.longitude);

        marcaRest.latitude=9.8998;
        marcaRest.longitude=-83.6789;
        marcaRest.name="vregv";

        MostarMarcador(googleMap,marcaRest.name,marcaRest.latitude,marcaRest.longitude);

        // Add a marker in Sydney and move the camera
        //ubicación usuario
        LatLng sydney = new LatLng(lat, lon);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));
        miUbicacion();



    }

// Marcar puntos

    public void MostarMarcador(GoogleMap googleMap,  String valorDesc, double valorLat, double valorLng)  {



        final LatLng punto1 = new LatLng(valorLat,valorLng);
        mMap.addMarker(new MarkerOptions().position(punto1).title(valorDesc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


       // sRest.add("0"+ " "+ valorDesc+" "+String.valueOf(valorLat)+ " "+String.valueOf(valorLng));

        int zoomlevel = 16;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punto1,zoomlevel));

//  **************************************************
// Se utilizará otro Evento  Click sobre el Marker - Restaurante en el mapa
//   ******************************************************
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Integer clickCount = (Integer) marker.getTag();
                if(doubleClick){
                    Intent det= new Intent(map.this, Details.class);
                    startActivity(det);
                }
                else{
                    doubleClick=true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleClick= false;
                        }
                    }, 1500);
                }
                Toast.makeText(getApplicationContext(),marcaRest.name, Toast.LENGTH_LONG).show();
                return false;
            }

        });



//   ******************************************************
// Se utilizará otro Evento para pintar  Marcas (marker) - Restaurante en el mapa
//   ******************************************************

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {


                Toast.makeText(getApplicationContext(),String.valueOf(latLng.latitude), Toast.LENGTH_LONG).show();


            }
        });






//   ******************************************************
// Se utilizará otro Evento global  onInfoWindowClick - Restaurante en el mapa
//   ******************************************************

/*     mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
         @Override
         public void onInfoWindowClick(Marker marker) {
             Toast.makeText(getApplicationContext()," onInforWindowClick", Toast.LENGTH_LONG).show();
         }
     });

*/

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(getApplicationContext(),"("+ String.valueOf(latLng.latitude)  + String.valueOf(latLng.longitude)+ ")", Toast.LENGTH_LONG).show();
            }
        });
    }


    // Evento: Gomap
    public void GoSearch(View view) {
        Intent siguiente = new Intent(this, Search.class);
        startActivity(siguiente);

    }

    public void Goadd(View view) {
        Intent siguiente = new Intent(this, Add.class);
        startActivity(siguiente);

    }

    public void restaurants(View view) {
        Intent siguiente = new Intent(this, restaurants.class);
        startActivity(siguiente);

    }
}
