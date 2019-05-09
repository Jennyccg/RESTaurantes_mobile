package com.example.mobile_rest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Search_Rest extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner precio;
    Spinner categoria;
    EditText nombre;
    //falta estrellas
    Double latitud;
    Double longitud;

    //guarda datos bot
    Double latW;
    Double longW;

    String preciesote;
    String cate;
    Rest_Data Rest= new Rest_Data();
    ArrayList<Rest_Data> todo_datos= new ArrayList<>();
    ListView lvRest;
    Adapter adaptadorRestaurante;
    public ArrayList<String> sRest = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__rest);
        lvRest = (ListView)findViewById(R.id.lv_mostrar);
        precio= findViewById(R.id.priceW);
        categoria=findViewById(R.id.tipo_comida);
        nombre=(EditText) findViewById(R.id.nomb);

        //datos
        latitud = getIntent().getDoubleExtra("lati",0.0);
        longitud = getIntent().getDoubleExtra("longi",0.0);


        ConnectAPI connectAPI = new ConnectAPI();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String session = sharedPreferences.getString("session", null);
        todo_datos = connectAPI.getAllStores(session);


        //Spinner
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


    }


public void filtrar(View view){
        sRest.clear();
        latW=0.0;
        longW= 0.0;
        boolean encontro = false;
        double distanciaLat;
        double distanciaLong;
        for (int k=0; k< todo_datos.size(); k++){

            if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase())){
                //Toast.makeText(getApplicationContext(),  String.valueOf(k)+ " categorias iguales" , Toast.LENGTH_LONG).show();
                sRest.add(todo_datos.get(k).name);

            }

            if (!cate.toUpperCase().equals("")){


                //Toast.makeText(getApplicationContext(),  String.valueOf(k)+ " categorias no vacio" , Toast.LENGTH_LONG).show();

                if (!preciesote.toUpperCase().equals("")){


                    if(!nombre.getText().toString().toUpperCase().equals("")){



                        if (true){

                            if (latW!= 0.0 && longW!=0.0){
                                Toast.makeText(getApplicationContext(),  String.valueOf(k)+ " loca no vacio" , Toast.LENGTH_LONG).show();
                                distanciaLat=Math.abs(latW- todo_datos.get(k).latitude);
                                distanciaLong=Math.abs(longW- todo_datos.get(k).longitude);

                                if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase()) && preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase()) && nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase())  && (distanciaLat<1 && distanciaLong<1) ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }


                            }else{

                                // es de loca
                            }


                        }else{
                            // es de estrellas
                        }


                    }else {
                        // es de nomb
                    }






                }else{
                    //  es de precio
                }


            }else{
                // es de categ
            }




            /*
            if (!preciesote.equals("")){

                if (!cate.equals("")){
                    if (!nombre.getText().toString().equals("")){

                        //no olvidar estrellas
                        if (todo_datos.get(k).price.toUpperCase()== preciesote.toUpperCase() && todo_datos.get(k).type.toUpperCase()==cate.toUpperCase() && todo_datos.get(k).name.toUpperCase() ==nombre.getText().toString().toUpperCase()){
                            sRest.add(todo_datos.get(k).name + " " + String.valueOf(todo_datos.get(k).score));
                        }
                    }else{
                        //Toast.makeText(getApplicationContext(), String.valueOf(k) + " "+ todo_datos.get(k).price.toUpperCase()+ "  "+  preciesote.toUpperCase().trim() , Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), cate , Toast.LENGTH_LONG).show();
                        //&&

                        if ((todo_datos.get(k).price.toUpperCase().trim ().equals(preciesote.toUpperCase().trim()) )   ){
                           // Toast.makeText(getApplicationContext(), todo_datos.get(k).price.toUpperCase().trim (), Toast.LENGTH_LONG).show();

                            sRest.add(todo_datos.get(k).name + " " + String.valueOf(todo_datos.get(k).score));
                        }


                    }

                }else{

                    if (todo_datos.get(k).price== preciesote ){
                        sRest.add(todo_datos.get(k).name + " " + String.valueOf(todo_datos.get(k).score));
                    }

                }

            } */
            //sRest.add(todo_datos.get(k).name + " " + String.valueOf(todo_datos.get(k).score));
        }

    adaptadorRestaurante = new ArrayAdapter(this,android.R.layout.simple_list_item_1, sRest);
    lvRest.setAdapter((ListAdapter) adaptadorRestaurante);
}


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if ( parent.getId()==R.id.priceW){
            preciesote = parent.getItemAtPosition(position).toString();
            //Toast.makeText(getApplicationContext(), preciesote, Toast.LENGTH_LONG).show();
        }
        else if (parent.getId()==R.id.tipo_comida){
            cate = parent.getItemAtPosition(position).toString();
            //Toast.makeText(getApplicationContext(), cate, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void tomaLocalizacion(View view){
        //no esta trayendo datos
        latW=latitud;
        Toast.makeText(getApplicationContext(),   " lat no vacio" + String.valueOf(latW)  , Toast.LENGTH_LONG).show();
        longW=longitud;
    }
}
