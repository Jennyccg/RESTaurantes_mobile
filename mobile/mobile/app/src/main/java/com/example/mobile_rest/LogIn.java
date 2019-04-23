package com.example.mobile_rest;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LogIn extends AppCompatActivity {

    public void recoverPassword(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Prueba");
        alertDialogBuilder.setMessage("La contraseña ha sido recuperada").setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void createAccount(View view){
        //Intent i = new Intent(this, CreateAccount.class);
        //startActivity(i);
    }

    public void syncFacebook(View view){

    }

    public void singIn(View view){
        Intent siguiente = new Intent(this, map.class);
        startActivity(siguiente);

    }

    public void enterApp(View view){
        //TODO Este metodo se usa para entrar a la app despues de verificar el usuario
        Intent i = new Intent(this, RestaurantInfo.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        findViewById(R.id.background).setBackgroundResource(R.mipmap.background);
    }
}
