package com.example.mobile_rest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class buscarRest extends AppCompatActivity {



    //  Declaracion Variables
        String tc_par ;
        double pre_par;
        int ubicacion_par;
        String nombre_par;
        int can_estr_par;


    // Declaracion de Receptores del Activity

    Spinner comboTCi;
    Spinner comboCEi;
    Spinner precio;
    EditText precioi;
    EditText ubicacioni;
    EditText nombrei;
    ListView lv_resti;

    ArrayList<String> arreglo = new ArrayList<String>();
    ArrayList<String> arreglo1 = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_rest);

        precio = (Spinner)findViewById(R.id.price);
        comboTCi = (Spinner)findViewById(R.id.combo_tc);
        //precioi = (EditText)findViewById(R.id.pc_dat);
        ubicacioni = (EditText)findViewById(R.id.ub_datkm);
        nombrei = (EditText)findViewById(R.id.nr_dat);
        comboCEi = (Spinner)findViewById(R.id.ce_datSP);
        lv_resti = (ListView)findViewById(R.id.lv_rest_dat);



        llenar_Precio ();
        LLenar_SpinnerComida();
       LLenar_SpinnerEstrellas();

    }

    private void llenar_Precio() {
        ArrayAdapter <String>  adaptador2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,  arreglo);
        arreglo.clear();
        arreglo.add("CHEAP");
        arreglo.add("AFFORDABLE");
        arreglo.add("EXPENSIVE");
    }




    private void LLenar_SpinnerComida(){

        ArrayAdapter <String>  adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,  arreglo);
        arreglo.clear();
        arreglo.add("FAST FOOD");
        arreglo.add("GOURMET");

    }

    private void LLenar_SpinnerEstrellas(){

        ArrayAdapter <String>  adaptador1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,  arreglo1);
        arreglo1.clear();
        arreglo1.add("Una");
        arreglo1.add("Dos");
        arreglo1.add("Tres");
        arreglo1.add("Cuatro");
        arreglo1.add("Cinco");
       comboCEi.setAdapter(adaptador1);

    }




    public void BuscarRestaurante(View view){

        Toast.makeText(this, "ingreso EXITOSA", Toast.LENGTH_SHORT).show();


        tc_par =comboTCi.getSelectedItem().toString();
        pre_par  = Double.parseDouble(precioi.getText().toString());
        ubicacion_par  = Integer.parseInt(ubicacioni.getText().toString());
        nombre_par = nombrei.getText().toString();
        switch (comboCEi.getSelectedItem().toString()){
            case "UNA":{
                can_estr_par  = 1;
                break;}
            case "DOS":{
                can_estr_par  = 2;
                break;}
            case "TRES":{
                can_estr_par  = 3;
                break;}
            case "CUATRO":{
                can_estr_par  = 4;
                break;}
            case "CINCO":{
                can_estr_par  = 5;
                break;}
                default:{
                    can_estr_par  = 5;
                    break;}


        }

        Toast.makeText(this, "antes EXITOSA", Toast.LENGTH_SHORT).show();

        ArrayList<String> Rest_Encontrados = new ArrayList<String>();

        ArrayAdapter adpatador3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1,Rest_Encontrados );
        //  Para prueba




        // ****************************************************************
        //                  AQUI VA LA FUNCION QUE HACE LOS GETS PARA OBTENER LOS RESTAURANTES
        //

      //            Rest_Encontrados = buscar_restaurants(tc_par, pre_par, ubicacion_par, nombre_par, can_estr_par);

        Rest_Encontrados.add("SubWay");
        Rest_Encontrados.add("McDonald");
        Rest_Encontrados.add("Burger King");



       lv_resti.setAdapter(adpatador3);

        Toast.makeText(this, "fin EXITOSA", Toast.LENGTH_SHORT).show();
    }

    // Esta funcion debe implementarse


    private ArrayList<String> buscar_restaurants(String tcpar, double prepar, int ubicacionpar, String nombrepar, int  canestpar){

        ArrayList<String> Restaurantes = new ArrayList<>();

        //  aqui va el codigo de la función para obtener restaurantes de la base de datos

        return  Restaurantes;
    }
}
