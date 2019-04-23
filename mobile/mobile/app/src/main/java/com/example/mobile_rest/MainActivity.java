package com.example.mobile_rest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createRestauranrDEBUG();
    }

    // Evento: Gomap
    public void Gomap(View view) {
        Intent siguiente = new Intent(this, map.class);
        startActivity(siguiente);
    }

    public void createRestauranrDEBUG(){
        Intent i = new Intent(this, CreateRestaurant.class);
        startActivity(i);
    }

    public void debugLogIn(){
        Intent i = new Intent(this, LogIn.class);
        startActivity(i);
    }

}
