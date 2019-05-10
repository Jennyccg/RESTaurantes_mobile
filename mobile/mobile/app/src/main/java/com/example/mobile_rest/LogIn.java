package com.example.mobile_rest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import android.support.v4.app.ActivityCompat;
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
        connectAPI.requestPassword(email.getText().toString());

        password.setText("");
        email.setText("");
    }

    public void createAccount(View view){
        Intent siguiente = new Intent(this, CreateAccount.class);
        startActivity(siguiente);
    }

    public void syncFacebook(View view){
        ConnectAPI connectAPI = new ConnectAPI();
        String id = connectAPI.getLastId();
        Log.i("ID", id);
    }

    public void singIn(View view){
        ConnectAPI connectAPI = new ConnectAPI();
        String session = connectAPI.logIn(email.getText().toString(), password.getText().toString());

        if(session == null){
            createAlertDialog("Error", "Correo o contraseñas erroneas.");
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("session", session);
            editor.putString("email", email.getText().toString());
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


        //imagen app
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

    }
}
