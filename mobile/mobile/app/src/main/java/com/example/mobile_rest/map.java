package com.example.mobile_rest;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
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

import java.util.ArrayList;

public class map extends FragmentActivity implements OnMapReadyCallback {
    // declaración variables
    private GoogleMap mMap;
    private Marker marcador ;
    Double latitudA;
    Double longitudA;
    String indice;
    // Areglo de restaurantes
    Rest_Data marcaRest= new Rest_Data();
    ArrayList <Rest_Data> puntos= new ArrayList<>();



    private final static int MY_PERMISSION_FINE_LOCATION = 101;


    boolean doubleClick=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        //Agrega restaurantes
        ConnectAPI connectAPI = new ConnectAPI();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String session = sharedPreferences.getString("session", null);
        puntos = connectAPI.getAllStores(session);


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
        //pintar puntos
    }


    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarMiUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    };






// geolocalización por el boton
    public void miUbicacion(){



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarMiUbicacion(location);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0, locListener);

    }

        /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Si hay permisos", Toast.LENGTH_LONG).show();
            mMap.setMyLocationEnabled(true);

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);

            }

        }*/





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

    //metodo en el cual se  presenta el mapa y la ubicación actual del usuario.
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
       // Toast.makeText(getApplicationContext(), "onmapredy", Toast.LENGTH_LONG).show();
        //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.builder().target));


        // POR AQUI TIENE QUE ESTAR  LO QUE SE LLENO DESDE LA BASE DE DATOS

        //PROCEDIMIENTO QUE MARCA
        allMarkers();
        // mo
        //marcaRest.latitude=9.86034;
        //marcaRest.longitude=-83.76850;
        //marcaRest.name="Pizza";

        //MostarMarcador(googleMap,marcaRest.name,marcaRest.latitude,marcaRest.longitude);

        //marcaRest.latitude=9.8998;
        //marcaRest.longitude=-83.6789;
        //marcaRest.name="vregv";

        //MostarMarcador(googleMap,marcaRest.name,marcaRest.latitude,marcaRest.longitude);

        // Add a marker in Sydney and move the camera
        //ubicación usuario
        LatLng sydney = new LatLng(lat, lon);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));
        miUbicacion();



    }
    //metodo que toma la ubicacion de los restaurantes
    public void allMarkers(){

        for(int k=0; k< puntos.size(); k++){
            //Toast.makeText(getApplicationContext(),String.valueOf(puntos.get(k).latitude)+ "_" +String.valueOf(puntos.get(k).longitude) , Toast.LENGTH_LONG).show();
            MostarMarcador(mMap,puntos.get(k).name,puntos.get(k).longitude,puntos.get(k).latitude);

        }

    }

    private void agregarMarcador(double latitudA, double longituA) {
        LatLng coordenadas = new LatLng(latitudA, longitudA);
        CameraUpdate miUbicacion;
        miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Mi posicion Actual"));
                //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        mMap.animateCamera(miUbicacion);


    }
    //  metodo actualiza la ubicación del usuario
    private void actualizarMiUbicacion(Location location) {
        if (location != null) {
            latitudA = location.getLatitude();
            longitudA = location.getLongitude();
            LatLng coordenadas = new LatLng(latitudA, longitudA);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 16));
            //                          agregarMarcador(latitudA, longitudA);

            //Toast.makeText(getApplicationContext(),String.valueOf(latitudA)+ "_" +String.valueOf(longitudA) , Toast.LENGTH_LONG).show();


        }
    }

// Marcar puntos
    //metodo en donde se toma la ubicación de los restaurante spara marcarlos en el mapa
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
                String nombresito;
                Integer clickCount = (Integer) marker.getTag();
                nombresito= marker.getTitle();
                for (int k=0; k<puntos.size(); k++){

                    if (nombresito.equals(puntos.get(k).name)){
                        indice= puntos.get(k).id;
                       // Toast.makeText(getApplicationContext(),"INDICE " + indice, Toast.LENGTH_LONG).show();
                    }
                }

                //puntos.get(Integer.parseInt(marker.getId())).id
                //Toast.makeText(getApplicationContext(),"antes double  " + marker.getId(), Toast.LENGTH_LONG).show();


                if(doubleClick){
                    //Toast.makeText(getApplicationContext(),"despues double", Toast.LENGTH_LONG).show();



                    //String indice = "";
                    Intent det= new Intent(map.this, RestaurantInfo.class);

                     //envio
                    det.putExtra("ID", indice);
                    startActivity(det);
                }
                else{
                    doubleClick=true;

                   // Toast.makeText(getApplicationContext(),"pone true double", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getApplicationContext(),"pone falso double", Toast.LENGTH_LONG).show();
                            doubleClick= false;
                        }
                    }, 2000);
                }

                return false;
            }

        });



//   ******************************************************
// Se utilizará otro Evento para pintar  Marcas (marker) - Restaurante en el mapa
//   ******************************************************

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {


               // Toast.makeText(getApplicationContext(),String.valueOf(latLng.latitude), Toast.LENGTH_LONG).show();


            }
        });







        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Toast.makeText(getApplicationContext(),"("+ String.valueOf(latLng.latitude)  + String.valueOf(latLng.longitude)+ ")", Toast.LENGTH_LONG).show();
            }
        });
    }


    // Evento: Gomap
    public void GoSearch(View view) {
        Intent siguiente = new Intent(this, Search_Rest.class);
        miUbicacion();
        //Toast.makeText(getApplicationContext(),String.valueOf(latitudA)  + String.valueOf(longitudA), Toast.LENGTH_LONG).show();
        siguiente.putExtra("lat",latitudA);
        siguiente.putExtra("longi",longitudA);
        startActivity(siguiente);

    }

    public void Goadd(View view) {
        Intent siguiente = new Intent(this, CreateRestaurant.class);
        miUbicacion();
        siguiente.putExtra("lat",latitudA);
        siguiente.putExtra("longi",longitudA);
        startActivity(siguiente);

    }

    public void restaurants(View view) {
        Intent siguiente = new Intent(this, restaurants.class);
        startActivity(siguiente);

    }

    public void coment(View view) {
        Intent siguiente = new Intent(this, RestaurantInfo.class);
        startActivity(siguiente);

    }
}
