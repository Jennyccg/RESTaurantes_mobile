package com.example.mobile_rest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;

public class CreateRestaurant extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_GET_IMG_FILE = 1;

    Rest_Data restaurant= new Rest_Data();

    EditText description;
    ImageView profile;
    ImageView newLocation;
    // Toma de datos
    Double latitud;
    Double longitud;
    EditText name;
    CheckBox lun;
    CheckBox mar;
    CheckBox mier;
    CheckBox jue;
    CheckBox vier;
    CheckBox sab;
    CheckBox dom;
    EditText horaI;
    EditText horaF;
    Spinner precio;
    Spinner categoria;
    String preciesote;
    String categoreichon;
    EditText contacto;
    EditText numero;







    private boolean checkComplition(){

        return true;
    }


    //Boton de success
    public void createRestaurant(View view){

        restaurant.name= name.getText().toString();
        restaurant.type=categoreichon;
        restaurant.price= preciesote;
        restaurant.latitude=latitud;
        restaurant.longitude=longitud;

        if (lun.isChecked()){
            restaurant.schedule.add("Lunes");
        }if (mar.isChecked()){
            restaurant.schedule.add("Martes");
        }if (mier.isChecked()){
            restaurant.schedule.add("Miercoles");
        }if (jue.isChecked()){
            restaurant.schedule.add("Jueves");
        }if (vier.isChecked()){
            restaurant.schedule.add("Viernes");
        }if (sab.isChecked()){
            restaurant.schedule.add("Sabado");
        }if (dom.isChecked()){
            restaurant.schedule.add("Domingo");
        }

        restaurant.nameContact.add(contacto.getText().toString());
        restaurant.valueContact.add(numero.getText().toString());

        //takeInfo ();
        Intent siguiente = new Intent(this, map.class);
        startActivity(siguiente);

    }
//____________________________________________________________________________________________________________________--
    public void getLocation(View view){

    }
// NO FUNCIONAAAAAA
    public void getImage(View view){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GET_IMG_FILE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }
        else {

        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_GET_IMG_FILE){

            Uri selectedImage = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePath[0]);
            String imgDecodableString = cursor.getString(columnIndex);

            cursor.close();

            profile.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

            Log.i("LOG_K", "Se escribio la imagen.");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        //datos
        latitud = getIntent().getDoubleExtra("lati",0.0);
        longitud = getIntent().getDoubleExtra("longi",0.0);
        name= findViewById(R.id.Name);
        lun= findViewById(R.id.lunes);
        mar= findViewById(R.id.Martes);
        mier= findViewById(R.id.Miercoles);
        jue= findViewById(R.id.Jueves);
        vier= findViewById(R.id.Viernes);
        sab= findViewById(R.id.Sabado);
        dom= findViewById(R.id.Domingo);
        horaI= findViewById(R.id.HoraInicio);
        horaF= findViewById(R.id.HoraFin);
        precio= findViewById(R.id.prices);
        categoria=findViewById(R.id.tipo);
        contacto=findViewById(R.id.contact);
        numero=findViewById(R.id.number);
        //______________________________________________________________________________

        ArrayAdapter <CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.prices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        precio.setAdapter(adapter);
        precio.setOnItemSelectedListener(this);

        //____________________________________________________________________________

        ArrayAdapter <CharSequence> adapter1= ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(adapter);
        categoria.setOnItemSelectedListener(this);




        profile = findViewById(R.id.newProfileImg);
        profile.setImageResource(R.mipmap.ic_launcher);
        restaurant = new Rest_Data();
        newLocation = (ImageView) findViewById(R.id.newLocation);

        // Tomar long y latitude
        newLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Funciono", Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if ( parent.getId()==R.id.prices){
            preciesote = parent.getItemAtPosition(position).toString();
        }
        else{
            categoreichon = parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
