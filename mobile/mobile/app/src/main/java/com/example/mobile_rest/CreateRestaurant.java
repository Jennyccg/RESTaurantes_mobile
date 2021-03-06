package com.example.mobile_rest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class CreateRestaurant extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_GET_IMG_FILE = 1;

    Rest_Data restaurant;
    ImageView newLocation;

    ArrayList<String> nameContact = new ArrayList<>();
    ArrayList<String> valueContact = new ArrayList<>();

    ArrayList<Bitmap> images = new ArrayList<>();

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
    Spinner estrellitaDondeEstas;
    String  estrellaAparecio;
    String categoreichon;
    EditText contacto;
    EditText numero;

    private boolean checkComplition(){
        return true;
    }


    public void createRestaurant(View view){

        restaurant.name= name.getText().toString();
        restaurant.type=categoreichon;
        restaurant.price= preciesote;
        restaurant.latitude=latitud;
        restaurant.longitude=longitud;

        if (lun.isChecked()){
            restaurant.schedule.add("MONDAY");
        }if (mar.isChecked()){
            restaurant.schedule.add("THUESDAY");
        }if (mier.isChecked()){
            restaurant.schedule.add("WENESDAY");
        }if (jue.isChecked()){
            restaurant.schedule.add("THURSDAY");
        }if (vier.isChecked()){
            restaurant.schedule.add("FRIDAY");
        }if (sab.isChecked()){
            restaurant.schedule.add("SATURDAY");
        }if (dom.isChecked()){
            restaurant.schedule.add("SUNDAY");
        }


        restaurant.setNameContact(nameContact);
        restaurant.setValueContact(valueContact);
        restaurant.score=Integer.parseInt(estrellaAparecio);


        String horaIniP  = horaI.getText().toString();
        int ind;
        ind = horaIniP.indexOf(":",0);
        restaurant.starHour = Integer.parseInt(horaIniP.substring(0,ind));
        restaurant.starMinute = Integer.parseInt(horaIniP.substring(ind+1,horaIniP.length()));


        String horaFinP  = horaF.getText().toString();
        ind = horaFinP.indexOf(":",0);
        restaurant.endHour = Integer.parseInt(horaFinP.substring(0,ind));
        restaurant.endMinute = Integer.parseInt(horaFinP.substring(ind+1,horaFinP.length()));


        restaurant.printObject();
        restaurant.getJson();

        Toast toast = Toast.makeText(this, "Subiendo restaurante...", Toast.LENGTH_SHORT);
        toast.show();

        uploadRestaurant(restaurant);


        //takeInfo ();

        toast = Toast.makeText(this, "Retaurante creado con exito.", Toast.LENGTH_SHORT);
        toast.show();

        Intent siguiente = new Intent(this, map.class);
        startActivity(siguiente);
    }


    private void uploadRestaurant(Rest_Data restaurant){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String session = sharedPreferences.getString("session", null);

        ConnectAPI connectAPI = new ConnectAPI();
        connectAPI.createRestaurant(restaurant, session, images);

        connectAPI = new ConnectAPI();
        String id = connectAPI.getLastId();

        for(int i = 0; i < images.size() ; ++i){
            connectAPI = new ConnectAPI();
            connectAPI.uploadPhoto(session, id, images.get(i));
        }
    }

//____________________________________________________________________________________________________________________--

    public void addContact(View view){
        TextView name = findViewById(R.id.contactNameEntry);
        TextView value = findViewById(R.id.valueEntry);

        nameContact.add(name.getText().toString());
        valueContact.add(value.getText().toString());


        TextView textView = new TextView(this);
        String contact = name.getText().toString() + ": " +value.getText().toString();
        textView.setText(contact);

        LinearLayout contacsLayout = findViewById(R.id.contactsLayout);
        contacsLayout.addView(textView);
    }

    public void getImage(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //sin permiso
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 782);
        } else {
            //con permiso
            cargarImagen();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions , int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 782) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Dieron permiso
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    cargarImagen();
                }
            }
        }
    }

    public void cargarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent,123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case 123:
                    Uri imageUri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);

                    cursor.close();

                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    images.add(bitmap);
                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(bitmap);

                    LinearLayout linearLayout = findViewById(R.id.imagesUploadScrollLayout);
                    linearLayout.addView(imageView);

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        //datos
        latitud = getIntent().getDoubleExtra("lat",0.0);
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
        estrellitaDondeEstas=findViewById(R.id.estre);
        numero=findViewById(R.id.number);

        //imagen app
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //______________________________________________________________________________

        ArrayAdapter <CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.prices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        precio.setAdapter(adapter);
        precio.setOnItemSelectedListener(this);

        //____________________________________________________________________________

        ArrayAdapter <CharSequence> adapter1= ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(adapter1);
        categoria.setOnItemSelectedListener(this);

        //____________________________________________________________________________

        ArrayAdapter <CharSequence> adapter2= ArrayAdapter.createFromResource(this, R.array.stars, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estrellitaDondeEstas.setAdapter(adapter2);
        estrellitaDondeEstas.setOnItemSelectedListener(this);


        restaurant = new Rest_Data();
        newLocation = (ImageView) findViewById(R.id.newLocation);

        // Tomar long y latitude
        newLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = "La localizacion actual es: latitud: " + latitud + ", longitud: " + longitud + ".";
                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId()==R.id.prices){
            preciesote = parent.getItemAtPosition(position).toString();
        }
        else if (parent.getId()==R.id.tipo){
            categoreichon = parent.getItemAtPosition(position).toString();
        }else{
            estrellaAparecio = parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
