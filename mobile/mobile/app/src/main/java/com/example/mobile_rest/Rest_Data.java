package com.example.mobile_rest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        Log.i("LOGRES", getId());
        Log.i("LOGRES", getName());
        Log.i("LOGRES", getType());
        Log.i("LOGRES", getPrice());
        //Log.i("LOGRES", imageUrl);
        Log.i("LOGRES", String.valueOf(longitude));
        Log.i("LOGRES", String.valueOf(latitude));
        Log.i("LOGRES", String.valueOf(starHour));
        Log.i("LOGRES", String.valueOf(starMinute));
        Log.i("LOGRES", String.valueOf(endHour));
        Log.i("LOGRES", String.valueOf(endMinute));

        for(int  i = 0; i < nameContact.size() ; ++i){
            Log.i("LOGRES", nameContact.get(i));
            Log.i("LOGRES", valueContact.get(i));
        }

        Log.i("LOGRES", String.valueOf(score));
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
                //String data = downloadTask.execute(url).get();
                bitmap = downloadImageTask.execute(url).get();
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

        String json = "";

        ArrayList<String> contacts = getNameContact();
        ArrayList<String> value = getValueContact();

        String contactsString = "contacts: [";
        for(int i = 0; i < contacts.size()-1 ; ++i){
            contactsString+= "{ \"name\": \"" + contacts.get(i) + "\", \"value\":\"" + value.get(i) + "\"},";
        }
        contactsString+= "{ \"name\": \"" + contacts.get(contacts.size()-1) + "\", \"value\":\"" + value.get(contacts.size()-1) + "\"}";
        contactsString += "]";

        Log.i("REST", contactsString);

        json = "{"+
                "name:\""+getName()+"\","+
                "type:\""+getType()+"\","+
                "price:\""+getPrice()+"\","+
                "location:{"+
                "type:\"Point\","+
                "coordinates:["+getLongitude()+","+getLatitude()+"]"+
                "},"+
                "schedule:{"+
                "MONDAY:{"+
                "start:{"+
                "hour:"+getStarHour()+","+
                "minute:"+getStarMinute()+""+
                "},"+
                "end:{"+
                "hour:"+getEndHour()+","+
                "minute:"+getEndMinute()+""+
                "}"+
                "},"+
                "TUESDAY:{"+
                "start:{"+
                "hour:"+getStarHour()+","+
                "minute:"+getStarMinute()+""+
                "},"+
                "end:{"+
                "hour:"+getEndHour()+","+
                "minute:"+getEndMinute()+""+
                "}"+
                "},"+
                "WEDNESDAY:{"+
                "start:{"+
                "hour:"+getStarHour()+","+
                "minute:"+getStarMinute()+""+
                "},"+
                "end:{"+
                "hour:"+getEndHour()+","+
                "minute:"+getEndMinute()+""+
                "}"+
                "},"+
                "THURSDAY:{"+
                "start:{"+
                "hour:"+getStarHour()+","+
                "minute:"+getStarMinute()+""+
                "},"+
                "end:{"+
                "hour:"+getEndHour()+","+
                "minute:"+getEndMinute()+""+
                "}"+
                "},"+
                "FRIDAY:{"+
                "start:{"+
                "hour:"+getStarHour()+","+
                "minute:"+getStarMinute()+""+
                "},"+
                "end:{"+
                "hour:"+getEndHour()+","+
                "minute:"+getEndMinute()+""+
                "}"+
                "}"+
                "},"+contactsString+
                "}";

        return json;
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

