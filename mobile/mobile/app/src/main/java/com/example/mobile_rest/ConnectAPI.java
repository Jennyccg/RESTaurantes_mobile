package com.example.mobile_rest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectAPI extends AsyncTask <String, String, String> {
    @Override
    protected String doInBackground(String... params){
        String urlString = params[0];
        OutputStream out = null;
        Log.i("Loga" , "Iniciando");
        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-type", "application/json");

            JSONObject respose = new JSONObject();
            OutputStream outputStream = urlConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(respose.toString());

            Log.i("Loga", respose.toString());


            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Done";
    }


}
