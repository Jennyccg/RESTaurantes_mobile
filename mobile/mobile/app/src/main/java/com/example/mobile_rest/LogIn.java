package com.example.mobile_rest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

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

        /*
        Usar base 64 para codificar las imagenes.
        Base64.encode(new File ("C:*Users*Kevin PC*Documents*GitHub*RESTaura*ic_launcher.png"));
        */
    }

    public void syncFacebook(View view){

        ConnectAPI connectAPI = new ConnectAPI();
        //Rest_Data restaurant = connectAPI.getRestaurant("5cbfae6f31fe3921c0943d98");
        //restaurant.setName("Otro restaurante.");
        //String json = restaurant.getJson();
        //Log.i("RESTJSON", json);

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String sesion = sharedPreferences.getString("session", null);
        //connectAPI.createRestaurant(restaurant, sesion);
        //connectAPI.uploadStars("5cbfae6f31fe3921c0943d98", "1", sesion);
        connectAPI.getAllStores(sesion);

    }

    public void singIn(View view){
        ConnectAPI connectAPI = new ConnectAPI();
        String session = connectAPI.logIn(email.getText().toString(), password.getText().toString());
        //Boolean success = connectAPI.logIn("kevin.daniel277@gmail.com", "345");

        if(session == null){
            createAlertDialog("Error", "Correo o contraseñas erroneas.");
        } else {
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("session", session);
            editor.commit();


            Intent siguiente = new Intent(this, map.class);
            startActivity(siguiente);
        }

    }

    public void enterApp(View view){
        //TODO Este metodo se usa para entrar a la app despues de verificar el usuario
        Intent i = new Intent(this, map.class);
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
