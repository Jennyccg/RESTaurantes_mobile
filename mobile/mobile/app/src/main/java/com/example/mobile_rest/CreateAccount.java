package com.example.mobile_rest;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CreateAccount extends AppCompatActivity {

    private void creatDialogBox(String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private boolean uploadAccount(String name, String password, String email){
        //Sube cuenta a API
        return true;
    }

    private void checkPassword(String name, String password, String rePassword, String email){

        if(password.matches(rePassword)){
            if(uploadAccount(name, password, rePassword)) {
                //Intent i = new Intent(this, RestaurantInfo.class);
                //startActivity(i);
            }

        } else {
            creatDialogBox("Contraseña incorrecta", "Las contraseñas que se escribieron no coinciden.");
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
            creatDialogBox("Espacio vacio", "Se ha dejado un espacio sin escribir.");
        } else {
            checkPassword(nameString, passwordString, rePasswordString, emailString);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }
}
