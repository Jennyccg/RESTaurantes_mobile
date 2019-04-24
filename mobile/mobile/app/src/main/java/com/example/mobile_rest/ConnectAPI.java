package com.example.mobile_rest;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;

import static android.content.Context.MODE_PRIVATE;

public class ConnectAPI extends AsyncTask <String, String, String> {
    @Override
    protected String doInBackground(String... urls){

        Log.i("Log", "Charging file.");
        String result = "";
        URL url;
        HttpURLConnection httpURLConnection;

        try {
            //Log.i("Loga", urls[0]);
            //Log.i("Loga", urls[1]);
            //Log.i("Loga", urls[2]);

            url = new URL(urls[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(urls[1]);

            if(urls[1] != "GET") {
                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                outputStreamWriter.write(urls[2]);
                outputStreamWriter.flush();
                outputStreamWriter.close();
                outputStream.close();
            }

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

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Log.i("Loga", result);
        /*
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
        Log.i("Log", "Finished parsing.");*/

    }

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


    public String logIn(String email, String password)  {
        String url = "http://restaurants-tec.herokuapp.com/users/sessions/"+email+"/"+password+"/REGULAR";
        String response = null;
        try {
            response = execute(url, "POST","").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String session = null;

        if(checkError(response)){
            try {
                JSONObject json = new JSONObject(response);
                json = new JSONObject(json.getString("data"));
                Log.i("Loga", json.getString("session"));
                session = json.getString("session");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return session;
    }

    public void requestPassword(String email){
        String url = "http://restaurants-tec.herokuapp.com/users/"+email;
        execute(url, "GET", "");
    }

    public boolean createAccount(String name, String email, String password){
        String url = "http://restaurants-tec.herokuapp.com/users/"+name+"/"+email+"/"+password;
        String response = null;
        try {
            response = execute(url, "POST", "").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return checkError(response);
    }


    //Devuelve una lista de Rest_Data con todos los restaurantes
    public ArrayList<Rest_Data> getAllStores(String session){
        String url = "http://restaurants-tec.herokuapp.com/restaurants/all";
        ArrayList<Rest_Data> restaurantes = new ArrayList<Rest_Data>();
        String response = null;


        try {
            response = execute(url, "GET", "").get();

            JSONObject json = new JSONObject(response);
            json = new JSONObject(json.getString("data"));

            JSONArray jsonArray = new JSONArray(json.getString("restaurants"));
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonElement = jsonArray.getJSONObject(i);
                //restaurantes.add(new Rest_Data(json.toString()));
                Log.i("Log", jsonElement.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return restaurantes;
    }

    //Crea un restaurante por medio de un objeto restaurant
    public boolean createRestaurant(Rest_Data restaurant, String session){
        String url = "http://restaurants-tec.herokuapp.com/restaurants/" + restaurant.getJson();
        String json = "{\"session\" : \""+session+"\"}";

        Log.i("RESTUpload", url);
        Log.i("RESTUpload", json);

        String response = null;

        try {
            response = execute(url, "POST", json).get();
            Log.i("RESTUpload", response);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }//checkError(response)

        return true;
    }

    //Obtiene el restaurante por id
    public Rest_Data getRestaurant(String id){
        String url = "http://restaurants-tec.herokuapp.com/restaurants/"+id;

        String response = null;
        try {
            response = execute(url, "GET", "").get();
            Log.i("REST", response);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Rest_Data restaurant = new Rest_Data(response);
        return restaurant;
    }


    public boolean uploadComment(String restaurantId, String comment, String session){
        String url = "http://restaurants-tec.herokuapp.com/restaurants/" + restaurantId+"/comments/"+comment;
        String json = "{\"session\" : \""+session+"\"}";
        String response = null;
        try {
            response = execute(url, "POST", json).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return checkError(response);
    }

    public ArrayList<String> getComments(String restaurantId){
        String url = "http://restaurants-tec.herokuapp.com/restaurants/"+ restaurantId+"/comments";

        String response = null;
        ArrayList<String> comments = new ArrayList<String>();

        try {
            response = execute(url, "GET", "").get();

            JSONObject dataJson = new JSONObject(response);
            dataJson = new JSONObject(dataJson.getString("data"));

            JSONArray jsonArray = new JSONArray(dataJson.getString("comments"));
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject json = jsonArray.getJSONObject(i);

                String name = json.getString("added_by");
                String message = json.getString("text");
                String finalMessage = name+ ": " + message;

                comments.add(finalMessage);
                Log.i("Log", finalMessage);
            }

            //session = json.getString("session");


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public boolean uploadStars(String restaurnatId, String score, String session){
        String url = "http://restaurants-tec.herokuapp.com/restaurants/"+restaurnatId+"/scores/"+score;
        String json = "{\"session\" : \""+session+"\"}";

        String response = null;
        try {
            response = execute(url, "POST", "").get();
            Log.i("LOGSTAR", response);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return checkError(response);

    }

    /*
    public void uploadPhoto(String session){
        Base64 image;
        Base64.getUrlEncoder().encodeToString(bytes);

        String url = "http://restaurants-tec.herokuapp.com/restaurants/"+"id"+"/images/"+"Base64";
        String json = "{\"session\" : \""+session+"\"}";

        String response = null;
        try {
            response = execute(url, "POST", json).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Base64.getUrlEncoder().encodeToString(bytes);
    }
    */
}
