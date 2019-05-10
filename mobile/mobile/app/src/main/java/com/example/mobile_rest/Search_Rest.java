package com.example.mobile_rest;

import android.content.Intent;
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
    String nombrelist;
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
        latitud = getIntent().getDoubleExtra("lat",0.0);
        longitud = getIntent().getDoubleExtra("longi",0.0);
        Toast.makeText(getApplicationContext(),String.valueOf(latitud)  + String.valueOf(longitud), Toast.LENGTH_LONG).show();


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

        boolean encontro = false;
        double distanciaLat=0.0;
        double distanciaLong=0.0;
        for (int k=0; k< todo_datos.size(); k++){

            /*
            if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase())){
                //Toast.makeText(getApplicationContext(),  String.valueOf(k)+ " categorias iguales" , Toast.LENGTH_LONG).show();
                sRest.add(todo_datos.get(k).name);

            }
            */

            if (!cate.toUpperCase().equals("")){


                //Toast.makeText(getApplicationContext(),  String.valueOf(k)+ " categorias no vacio" , Toast.LENGTH_LONG).show();

                if (!preciesote.toUpperCase().equals("")){


                    if(!nombre.getText().toString().toUpperCase().equals("")){



                        if (true){

                            if (latW!= 0.0 && longW!=0.0){
                                Toast.makeText(getApplicationContext(),  String.valueOf(k)+ " loca no vacio" , Toast.LENGTH_LONG).show();
                                distanciaLat=Math.abs(latW- todo_datos.get(k).latitude);
                                distanciaLong=Math.abs(longW- todo_datos.get(k).longitude);
                                //falta estrellas
                                if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase()) && preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase()) && nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase())  && (distanciaLat<1 && distanciaLong<1) ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }


                            }else{

                                if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase()) && preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase()) && nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase())){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }
                            }


                        }else{
                            if (latW!= 0.0 && longW!=0.0){

                                if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase()) && preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase()) && nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase()) && (distanciaLat<1 && distanciaLong<1)){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }

                            }else {
                                if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase()) && preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase()) && nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase()) ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }

                            }


                        }


                    }else {
                        // es de nomb

                        if (true){

                            if (latW!= 0.0 && longW!=0.0){
                                //meter estrellas

                                if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase()) && preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase()) && (distanciaLat<1 && distanciaLong<1) ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }

                            }else {
                                //falta estrellas
                                if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase()) && preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase())){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }

                            }


                        }else{
                            //estrellas

                            if (latW!= 0.0 && longW!=0.0){

                                if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase()) && preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase()) && (distanciaLat<1 && distanciaLong<1)  ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }


                            }else {
                                //vacio
                            }
                        }
                    }






                }else{
                    //  es de precio
                    if (!nombre.getText().toString().toUpperCase().equals("")){

                        if (true){

                            if (latW!= 0.0 && longW!=0.0){
                                //meter estrellas

                                if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase())  && nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase()) && (distanciaLat<1 && distanciaLong<1) ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }

                            }else {
                                //falta estrellas
                                if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase()) && nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase())){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }

                            }


                        }else{

                            // de las estrellas
                            if (latW!= 0.0 && longW!=0.0){

                                if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase())  && nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase()) && (distanciaLat<1 && distanciaLong<1) ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }

                            }else{

                                //limpio
                                if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase())  && nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase()) ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }
                            }

                        }


                    }else{
                        // else del nombre dif null


                        if (true){
                            if (latW!= 0.0 && longW!=0.0){
                                //falta estrellas
                                if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase())  && (distanciaLat<1 && distanciaLong<1) ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }



                            }else{
                                    //falta estrellas
                                if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase()) ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }

                            }



                        }else{
                            if (latW!= 0.0 && longW!=0.0){
                                if (cate.toUpperCase().equals(todo_datos.get(k).type.toUpperCase())  && (distanciaLat<1 && distanciaLong<1) ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }
                            }
                        }
                    }

                }


            }else{
                // es de categ

                if (!preciesote.toUpperCase().equals("")){


                    if(!nombre.getText().toString().toUpperCase().equals("")){



                        if (true){

                            if (latW!= 0.0 && longW!=0.0){
                                Toast.makeText(getApplicationContext(),  String.valueOf(k)+ " loca no vacio" , Toast.LENGTH_LONG).show();
                                distanciaLat=Math.abs(latW- todo_datos.get(k).latitude);
                                distanciaLong=Math.abs(longW- todo_datos.get(k).longitude);
                                //falta estrellas
                                if (preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase()) && nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase())  && (distanciaLat<1 && distanciaLong<1) ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }


                            }else{

                                if (preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase()) && nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase())){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }
                            }


                        }else{
                            if (latW!= 0.0 && longW!=0.0){

                                if (preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase()) && nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase()) && (distanciaLat<1 && distanciaLong<1)){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }

                            }else {
                                if (preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase()) && nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase()) ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }

                            }


                        }


                    }else {
                        // es de nomb

                        if (true){

                            if (latW!= 0.0 && longW!=0.0){
                                //meter estrellas

                                if (preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase()) && (distanciaLat<1 && distanciaLong<1) ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }

                            }else {
                                //falta estrellas
                                if ( preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase())){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }

                            }


                        }else{
                            //estrellas

                            if (latW!= 0.0 && longW!=0.0){

                                if (preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase()) && (distanciaLat<1 && distanciaLong<1)  ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }


                            }else {
                                //vacio
                            }
                        }
                    }






                }else{
                    //sin precio
                    if(!nombre.getText().toString().toUpperCase().equals("")){



                        if (true){

                            if (latW!= 0.0 && longW!=0.0){
                                Toast.makeText(getApplicationContext(),  String.valueOf(k)+ " loca no vacio" , Toast.LENGTH_LONG).show();
                                distanciaLat=Math.abs(latW- todo_datos.get(k).latitude);
                                distanciaLong=Math.abs(longW- todo_datos.get(k).longitude);
                                //falta estrellas
                                if (nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase())  && (distanciaLat<1 && distanciaLong<1) ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }


                            }else{

                                if ( nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase())){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }
                            }


                        }else{
                            if (latW!= 0.0 && longW!=0.0){

                                if (preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase()) && nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase()) && (distanciaLat<1 && distanciaLong<1)){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }

                            }else {
                                if (preciesote.toUpperCase().equals(todo_datos.get(k).price.toUpperCase()) && nombre.getText().toString().toUpperCase().equals(todo_datos.get(k).name.toUpperCase()) ){
                                    encontro=true;
                                    sRest.add(todo_datos.get(k).name);


                                }else{
                                    //de la comparacion
                                }

                            }


                        }


                    }else {
                        Toast.makeText(getApplicationContext(),"ENTRO1 " + "lat"+ String.valueOf(latW)+ "long" + String.valueOf(longW),  Toast.LENGTH_LONG).show();

                        if (latW!= 0.0 && longW!=0.0){

                            distanciaLat=Math.abs(latW- todo_datos.get(k).latitude);
                            distanciaLong=Math.abs(longW- todo_datos.get(k).longitude);
                            Toast.makeText(getApplicationContext(),"ENTRO " + String.valueOf(todo_datos.get(k).latitude) + "hjgjg"+ String.valueOf(todo_datos.get(k).longitude) ,  Toast.LENGTH_LONG).show();
                            if ((distanciaLat<93.7 && distanciaLong<93.7)){
                                encontro=true;
                                sRest.add(todo_datos.get(k).name);


                            }else{
                                //de la comparacion
                            }

                        }

                    }

                }

            }

        }

    adaptadorRestaurante = new ArrayAdapter(this,android.R.layout.simple_list_item_1, sRest);
    lvRest.setAdapter((ListAdapter) adaptadorRestaurante);

    lvRest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int idRest;

            nombrelist = sRest.get(position);
            nextW(position);

        }
    });
}

    public void nextW(int posicion){

       String indice="";
        for (int k=0; k<todo_datos.size(); k++){

            if (nombrelist.equals(todo_datos.get(k).name)){
                indice= todo_datos.get(k).id;
                // Toast.makeText(getApplicationContext(),"INDICE " + indice, Toast.LENGTH_LONG).show();
            }
        }

        Toast.makeText(this, todo_datos.get(posicion).getId(), Toast.LENGTH_SHORT).show();

        Intent siguiente = new Intent( this,RestaurantInfo.class);

        // envia un parametros
        siguiente.putExtra("ID", indice);


        startActivity(siguiente);

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
        longW=longitud;
        Toast.makeText(getApplicationContext(),   " lat no vacio" + String.valueOf(longW)  , Toast.LENGTH_LONG).show();

    }


}
