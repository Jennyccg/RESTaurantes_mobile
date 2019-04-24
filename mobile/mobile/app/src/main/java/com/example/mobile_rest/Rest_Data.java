package com.example.mobile_rest;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Rest_Data {
    String id;
    String name;
    String type;
    String price;
    Double latitude;
    Double longitude;
    ArrayList <String> schedule;
    int starHour;
    int starMinute;
    int endHour;
    int endMinute;
    ArrayList <String> nameContact;
    ArrayList  <String> valueContact;
     int score;


    public Rest_Data(String data){
        JSONObject json = null;
        try {
            json = new JSONObject(data);

            setId(json.getString("id"));
            setName(json.getString("name"));

            //No se como hacer estos
            setEndHour(Integer.parseInt(json.getString("success")));
            setEndMinute(Integer.parseInt(json.getString("success")));
            setStarHour(Integer.parseInt(json.getString("success")));
            setStarMinute(Integer.parseInt(json.getString("success")));
            //

            JSONObject location = new JSONObject(json.getString("location"));
            setLatitude(Double.valueOf(location.getString("latitud")));
            setLongitude(Double.valueOf(location.getString("longitud")));

            setNameContact(null);

            setPrice(json.getString("price"));

            setSchedule(null);


            setType(json.getString("type"));

            setValueContact(null);

            //json.getString("success")

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
        /*
        json = "" +
                "{\n" +
                "        name: "+getName()+",\n" +
                "        type: "+getType()+",\n" +
                "        price: "+getPrice()+",\n" +
                "        location: {\n" +
                "            latitude: "+getLatitude()+",\n" +
                "            longitude: "+getLongitude()+"\n" +
                "        },\n" +
                "        schedule: [\n" +
                "            MONDAY: { \n" +
                "                start: {\n" +
                "                    hour: "+int+", \n" +
                "                    minute: "+int+" \n" +
                "                }\n" +
                "                end: {\n" +
                "                    hour: "+int +", \n" +
                "                    minute: "+int+" \n" +
                "                }\n" +
                "            }\n" +
                "            ...same for every other day of the week\n" +
                "        ],\n" +
                "        contacts: [\n" +
                "            {\n" +
                "                name: "+string+",\n" +
                "                value: "+string+"\n" +
                "            },\n" +
                "            ...n contacts\n" +
                "        ]\n" +
                "    }";
        */
        return json;
    }

    public int getScore() {
        return score;
    }

    public void setScore (int score){
        this.score = score;
    }
}

