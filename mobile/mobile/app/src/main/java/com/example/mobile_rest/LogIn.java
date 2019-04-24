package com.example.mobile_rest;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class LogIn extends AppCompatActivity {

    TextView email;
    TextView password;


    private void createAlertDialog(String title, String content){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(content).setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void recoverPassword(View view){
        ConnectAPI connectAPI = new ConnectAPI();
        createAlertDialog("Contraseña recuperada", "Su contraseña a sido enviada al correo.");
        connectAPI.requestPassword("kevin.daniel277@gmail.com");

        password.setText("");
        email.setText("");
    }

    public void createAccount(View view){
        Intent siguiente = new Intent(this, CreateAccount.class);
        startActivity(siguiente);
    }

    public void syncFacebook(View view){

    }

    public void singIn(View view){
        ConnectAPI connectAPI = new ConnectAPI();
        Boolean success = connectAPI.logIn(email.getText().toString(), password.getText().toString());
        //Boolean success = connectAPI.logIn("kevin.daniel277@gmail.com", "345");


        if(success) {
            Intent siguiente = new Intent(this, map.class);
            startActivity(siguiente);
        } else {
            createAlertDialog("Error", "Correo o contraseñas erroneas.");
        }
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

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.logInPassword);

    }
}
