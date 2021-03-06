package com.example.mobile_rest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CreateAccount extends AppCompatActivity {

    private void createDialogBox(String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private boolean logIn(String email, String password){
        ConnectAPI connectAPI = new ConnectAPI();
        String session = connectAPI.logIn(email, password);

        if(session == null){
            return false;
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("session", session);
            editor.putString("email", email);
            editor.commit();
            return true;
        }
    }

    private boolean uploadAccount(String name, String password, String email){
        ConnectAPI connectAPI = new ConnectAPI();
        return connectAPI.createAccount(name, email, password);
    }

    private void checkPassword(String name, String password, String rePassword, String email){

        if(password.matches(rePassword)){
            if(uploadAccount(name, password, email)) {
                if(logIn(email, password)) {

                    Intent i = new Intent(this, RestaurantInfo.class);
                    startActivity(i);

                } else {
                    createDialogBox("Error", "Ocurrio un problema iniciando sesión.");
                }
            } else {
                createDialogBox("Error", "Ocurrio un problema creando la cuenta.");
            }

        } else {
            createDialogBox("Contraseña incorrecta", "Las contraseñas que se escribieron no coinciden.");
        }
    }

    public void createAccount(View view){

        TextView password = (TextView)findViewById(R.id.passwordText);
        TextView rePassword = (TextView)findViewById(R.id.rePasswordText);
        TextView name = (TextView)findViewById(R.id.nameText);
        TextView email = (TextView)findViewById(R.id.emailText);


        String passwordString = password.getText().toString();
        String rePasswordString = rePassword.getText().toString();
        String nameString = name.getText().toString();
        String emailString = email.getText().toString();

        if(passwordString.isEmpty() || rePasswordString.isEmpty() || nameString.isEmpty() || emailString.isEmpty()){
            createDialogBox("Espacio vacio", "Se ha dejado un espacio sin escribir.");
        } else {
            checkPassword(nameString, passwordString, rePasswordString, emailString);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //imagen app
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }
}
