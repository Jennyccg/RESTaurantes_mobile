package com.example.mobile_rest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import static java.util.Arrays.asList;

public class Rest_Data {
    String id;
    String name;
    String type;
    String price;
    Double latitude;
    Double longitude;
    float score;
    int starHour;
    int starMinute;
    int endHour;
    int endMinute;
    ArrayList <String>  schedule = new ArrayList<>();
    ArrayList <String> nameContact = new ArrayList<>();
    ArrayList  <String> valueContact = new ArrayList<>();
    ArrayList <String> imageUrl = new ArrayList<>();



    public Rest_Data(){
        this.id = "0";
    }

    //Creates object by copying information from json
    public Rest_Data(String data){
        JSONObject json = null;

        try {
            json = new JSONObject(data);

            //Set id, name, score
            setId(json.getString("_id"));
            setName(json.getString("name"));
            setScore(Float.parseFloat(json.getString("score")));


            //Set days of schedule
            JSONObject scheduleJson = new JSONObject(json.getString("schedule"));
            ArrayList<String> days = new ArrayList<>();
            for(Iterator key = scheduleJson.keys(); key.hasNext();){
                String keyString = String.valueOf(key.next());
                days.add(keyString);
            }
            setSchedule(days);
            JSONObject day = (JSONObject) scheduleJson.get(days.get(0));

            JSONObject start = new JSONObject(day.getString("start"));
            JSONObject end = new JSONObject(day.getString("end"));

            setEndHour(Integer.parseInt(end.getString("hour")));
            setEndMinute(Integer.parseInt(end.getString("minute")));
            setStarHour(Integer.parseInt(start.getString("hour")));
            setStarMinute(Integer.parseInt(start.getString("minute")));


            //Set location, latitud, longitud
            JSONObject location = new JSONObject(json.getString("location"));
            JSONArray coordinates = new JSONArray(location.getString("coordinates"));
            setLatitude(coordinates.getDouble(1));
            setLongitude(coordinates.getDouble(0));


            //Set price, type
            setPrice(json.getString("price"));
            setType(json.getString("type"));


            //Set contacts
            JSONArray contacts = new JSONArray(json.getString("contacts"));
            ArrayList <String> nameContact = new ArrayList<String>();
            ArrayList  <String> valueContact = new ArrayList<String>();
            for (int i = 0; i < contacts.length(); i++){
                JSONObject contactsJson = contacts.getJSONObject(i);
                valueContact.add(contactsJson.getString("value"));
                nameContact.add(contactsJson.getString("name"));
            }
            setNameContact(nameContact);
            setValueContact(valueContact);


            //Set image

            JSONArray imageArray = new JSONArray(json.getString("images"));
            for(int i = 0; i < imageArray.length(); ++i) {
                this.imageUrl.add(imageArray.getString(i));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void printObject(){
        Log.i("LOGRES_ID", getId());
        Log.i("LOGRES_NAME", getName());
        Log.i("LOGRES_SCORE", String.valueOf(score));
        Log.i("LOGRES_TYPE", getType());
        Log.i("LOGRES_PRICE", getPrice());
        Log.i("LOGRES_LONGITUDE", String.valueOf(longitude));
        Log.i("LOGRES_LATITUD", String.valueOf(latitude));
        Log.i("LOGRES_STARTHOUR", String.valueOf(starHour));
        Log.i("LOGRES_STARTMINUTE", String.valueOf(starMinute));
        Log.i("LOGRES_ENDHOUR", String.valueOf(endHour));
        Log.i("LOGRES_ENDMINUTE", String.valueOf(endMinute));

        for(int  i = 0; i < nameContact.size() ; ++i){
            Log.i("LOGRES_NAMECONTACT", nameContact.get(i));
            Log.i("LOGRES_VALUECONTACT", valueContact.get(i));
        }

        for(int  i = 0; i < schedule.size() ; ++i){
            Log.i("LOGRES_SCHEDULE", schedule.get(i));
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String result = "";

            try {
                Document document = Jsoup.connect(urls[0]).ignoreContentType(true).get();
                result = document.toString();
                return result;
            }
            catch (Exception e){
                e.printStackTrace();
                return "Error";
            }
        }
    }
    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap = null;

            try {
                InputStream inputStream = new java.net.URL(urls[0]).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);

            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }

    public ArrayList<Bitmap> getAllBitmaps(){
        String url = "";
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for(int i = 0; i < imageUrl.size(); ++i) {
            url = imageUrl.get(i);
            DownloadTask downloadTask = new DownloadTask();
            DownloadImageTask downloadImageTask = new DownloadImageTask();
            Bitmap bitmap = null;
            try {
                bitmap = downloadImageTask.execute(url).get();

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                if(width > 1080 || height > 720){
                    width = (int) (0.4 * width);
                    height = (int) (0.4 * height);
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                }
                bitmaps.add(bitmap);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return bitmaps;
    }

    public String getJson() {

        String jsonString = "";

        JSONObject json = new JSONObject();

        try {
            json.put("name", getName());
            json.put("type", getType());
            json.put("price", getPrice());

            JSONObject temporalJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            temporalJson.put("type", "Point");

            jsonArray.put(getLatitude());
            jsonArray.put(getLatitude());
            temporalJson.put("coordinates", jsonArray); //TODO
            json.put("location", temporalJson);

            temporalJson = new JSONObject();
            for(int i = 0; i < getSchedule().size() ; ++i){
                JSONObject dayJson = new JSONObject();

                JSONObject timeJson = new JSONObject();
                timeJson.put("hour", starHour);
                timeJson.put("minute", starMinute);

                dayJson.put("start", timeJson);

                timeJson = new JSONObject();
                timeJson.put("hour", endHour);
                timeJson.put("minute", endMinute);

                dayJson.put("end", timeJson);

                temporalJson.put(getSchedule().get(i), dayJson);
            }

            json.put("schedule", temporalJson);

            jsonArray = new JSONArray();
            for (int i = 0 ;  i < getNameContact().size() ; ++i){
                temporalJson = new JSONObject();
                temporalJson.put("name", getNameContact().get(i));
                temporalJson.put("value", getValueContact().get(i));
                jsonArray.put(temporalJson);
            }

            json.put("contacts", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonString = json.toString();
        Log.i("RESTAURANT_JSON", jsonString);
        return jsonString;
    }

    public String getScheduleTime(){
        String scheduleTime = "";

        String hour = "";
        String minute = "";
        if(starHour < 10){
            hour = "0"+starHour;
        } else hour = String.valueOf(starHour);

        if(starMinute < 10){
            minute = "0"+starMinute;
        } else minute = String.valueOf(starMinute);

        scheduleTime = hour+":"+minute;

        if(endHour < 10){
            hour = "0"+endHour;
        } else hour = String.valueOf(endHour);
        if(endMinute < 10){
            minute = "0"+endMinute;
        } else minute = String.valueOf(endMinute);
        scheduleTime+= " - " + hour+":"+minute;

        return scheduleTime;
    }

    //------ Getters and Setters
    //Score
    public float getScore() {
        return score;
    }
    public void setScore (float score){
        this.score = score;
    }

    //ID
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    //Name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    //Type
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    //Price
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    //Latitud
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    //Longitud
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    //Schedule
    public ArrayList<String> getSchedule() {
        return schedule;
    }
    public void setSchedule(ArrayList<String> schedule) {
        this.schedule = new ArrayList<>(schedule);
    }

    //Start hour
    public int getStarHour() {
        return starHour;
    }
    public void setStarHour(int starHour) {
        this.starHour = starHour;
    }

    //Start Minute
    public int getStarMinute() {
        return starMinute;
    }
    public void setStarMinute(int starMinute) {
        this.starMinute = starMinute;
    }

    //End Hour
    public int getEndHour() {
        return endHour;
    }
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    //End Minute
    public int getEndMinute() {
        return endMinute;
    }
    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    //Name Contact
    public ArrayList<String> getNameContact() {
        return nameContact;
    }
    public void setNameContact(ArrayList<String> nameContact) {
        this.nameContact = nameContact;
    }

    //Value contact
    public ArrayList<String> getValueContact() {
        return valueContact;
    }
    public void setValueContact(ArrayList<String> valueContact) {
        this.valueContact = valueContact;
    }

    //Image
    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = new ArrayList<>(imageUrl);
    }
}

