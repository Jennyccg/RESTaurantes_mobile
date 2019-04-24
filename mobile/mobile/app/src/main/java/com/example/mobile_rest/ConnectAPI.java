package com.example.mobile_rest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.w3c.dom.Document;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;

public class ConnectAPI extends AsyncTask <String, String, String> {
    @Override
    protected String doInBackground(String... urls){

        Log.i("Log", "Charging file.");
        String result = "";
        URL url;
        HttpURLConnection httpURLConnection;

        try {
            Log.i("Loga", urls[1]);
            url = new URL(urls[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(urls[1]);
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            int data = inputStreamReader.read();
            while (data != -1){
                char current = (char)data;
                result += current;
                data = inputStreamReader.read();
            }
            Log.i("Log", "Charging success.");
            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Log", "Charging error.");

        return null;
    }
/*
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.i("Loga", result);

        try {

            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject json = jsonArray.getJSONObject(i);
                Log.i("Log", json.getString("data"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Log", "Finished parsing.");

    }*/

    private boolean checkError(String data){
        Log.i("Loga", data);


        if(data == null){
            return false;
        }

        try {
            JSONObject json = new JSONObject(data);
            Log.i("Loga", json.getString("success"));

            if(json.getString("success") == "false"){
                Log.i("Loga", "failed");
                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Loga", "win");
        return true;
    }

    public boolean logIn(String email, String password)  {
        String url = "http://restaurants-tec.herokuapp.com/users/sessions/"+email+"/"+password+"/REGULAR";
        String response = null;
        try {
            response = execute(url, "POST").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return checkError(response);
    }

    public void requestPassword(String email){
        String url = "http://restaurants-tec.herokuapp.com/users/"+email;
        execute(url, "GET");
    }

    public boolean createAccount(String name, String email, String password){
        String url = "http://restaurants-tec.herokuapp.com/users/"+name+"/"+email+"/"+password;
        String response = null;
        try {
            response = execute(url, "POST").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return checkError(response);
    }


}
