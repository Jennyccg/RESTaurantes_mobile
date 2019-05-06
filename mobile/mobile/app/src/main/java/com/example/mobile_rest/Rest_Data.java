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
    String imageUrl;
    Double latitude;
    Double longitude;
    ArrayList <String>  schedule = new ArrayList<String>();
    int starHour;
    int starMinute;
    int endHour;
    int endMinute;
    ArrayList <String> nameContact = new ArrayList<String>();
    ArrayList  <String> valueContact = new ArrayList<String>();
    float score;



    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String result = "";

            try {
                Document document = Jsoup.connect(urls[0]).get();
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

    private Bitmap getImage(){
        String url = imageUrl;

        DownloadTask downloadTask = new DownloadTask();
        DownloadImageTask downloadImageTask = new DownloadImageTask();
        Bitmap bitmap = null;
        try {
            String data = downloadTask.execute(url).get();
            bitmap = downloadImageTask.execute(data).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public Rest_Data(){

    }

    public Rest_Data(String data){

        JSONObject json = null;

        try {
            json = new JSONObject(data);

            setId(json.getString("_id"));
            setName(json.getString("name"));
            setScore(Float.parseFloat(json.getString("score")));


            JSONObject schedule = new JSONObject(json.getString("schedule"));
            ArrayList<String> days = new ArrayList<>();
            for(Iterator key = schedule.keys(); key.hasNext();){
                String keyString = String.valueOf(key.next());
                days.add(keyString);
                //Log.i("DAYS_JSON_READER", keyString);
            }

            JSONObject day = (JSONObject) schedule.get(days.get(0));

            JSONObject start = new JSONObject(day.getString("start"));
            JSONObject end = new JSONObject(day.getString("end"));

            setEndHour(Integer.parseInt(end.getString("hour")));
            setEndMinute(Integer.parseInt(end.getString("minute")));
            setStarHour(Integer.parseInt(start.getString("hour")));
            setStarMinute(Integer.parseInt(start.getString("minute")));

            setSchedule((ArrayList<String>) days.clone());

            JSONObject location = new JSONObject(json.getString("location"));
            JSONArray coordinates = new JSONArray(location.getString("coordinates"));
            setLatitude(coordinates.getDouble(1));
            setLongitude(coordinates.getDouble(0));


            setPrice(json.getString("price"));
            setType(json.getString("type"));

            setValueContact(null);
            setNameContact(null);

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

            setSchedule(null);

            /*
            JSONArray jsonArrayA = new JSONArray("images");
            JSONObject image = jsonArrayA.getJSONObject(0);
            this.imageUrl = image.toString();
            */

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*

        Log.i("LOGRES", "Se hizo el restaurante.");

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
        */
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public ArrayList<String> getSchedule() {
        return schedule;
    }

    public void setSchedule(ArrayList<String> schedule) {
        this.schedule = schedule;
    }

    public int getStarHour() {
        return starHour;
    }

    public void setStarHour(int starHour) {
        this.starHour = starHour;
    }

    public int getStarMinute() {
        return starMinute;
    }

    public void setStarMinute(int starMinute) {
        this.starMinute = starMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public ArrayList<String> getNameContact() {
        return nameContact;
    }

    public void setNameContact(ArrayList<String> nameContact) {
        this.nameContact = nameContact;
    }

    public ArrayList<String> getValueContact() {
        return valueContact;
    }

    public void setValueContact(ArrayList<String> valueContact) {
        this.valueContact = valueContact;
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


        /*json = "{\n" +
                "        \"name\": \""+getName()+"\",\n" +
                "        \"type\": \""+getType()+"\",\n" +
                "        \"price\": \""+getPrice()+"\",\n" +
                "        \"location\": {\n" +
            "               \"type\": \"Point\"," +
                "            \"coordinates\" : ["+getLongitude()+","+getLatitude()+"]\n" +
                "        },\n" +
                "        \"schedule\": {\n" +
                "            \"MONDAY\": { \n" +
            "                \"start\": {\n" +
                    "                    \"hour\": "+getStarHour()+", \n" +
                    "                    \"minute\": "+getStarMinute()+" \n" +
                    "                }\n" +
                    "                \"end\": {\n" +
                    "                    \"hour\": "+getEndHour() +", \n" +
                    "                    \"minute\": "+getEndMinute()+" \n" +
                    "                }\n" +
                    "            },\n" +
                "            \"TUESDAY\": { \n" +
                "                \"start\": {\n" +
                "                    \"hour\": "+getStarHour()+", \n" +
                "                    \"minute\": "+getStarMinute()+" \n" +
                "                }\n" +
                "                \"end\": {\n" +
                "                    \"hour\": "+getEndHour() +", \n" +
                "                    \"minute\": "+getEndMinute()+" \n" +
                "                }\n" +
                "            },\n" +
                "            \"WEDNESDAY\": { \n" +
                "                \"start\": {\n" +
                "                    \"hour\": "+getStarHour()+", \n" +
                "                    \"minute\": "+getStarMinute()+" \n" +
                "                }\n" +
                "                \"end\": {\n" +
                "                    \"hour\": "+getEndHour() +", \n" +
                "                    \"minute\": "+getEndMinute()+" \n" +
                "                }\n" +
                "            },\n" +
                "            \"THURSDAY\": { \n" +
                "                \"start\": {\n" +
                "                    \"hour\": "+getStarHour()+", \n" +
                "                    \"minute\": "+getStarMinute()+" \n" +
                "                }\n" +
                "                \"end\": {\n" +
                "                    \"hour\": "+getEndHour() +", \n" +
                "                    \"minute\": "+getEndMinute()+" \n" +
                "                }\n" +
                "            },\n" +
                "            \"FRIDAY\": { \n" +
                "                \"start\": {\n" +
                "                    \"hour\": "+getStarHour()+", \n" +
                "                    \"minute\": "+getStarMinute()+" \n" +
                "                }\n" +
                "                \"end\": {\n" +
                "                    \"hour\": "+getEndHour() +", \n" +
                "                    \"minute\": "+getEndMinute()+" \n" +
                "                }\n" +
                "            }\n" +
                "        },\n" + contactsString +
                "    }";

*/
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

    public float getScore() {
        return score;
    }

    public void setScore (float score){
        this.score = score;
    }
}

