package com.example.mobile_rest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;

public class RestaurantInfo extends AppCompatActivity {

    String id;

    //Get from API and Set on app
    private void setInformation(){
        downloadAPI();
        updateComments();
        setStars(0);
    }

    private void downloadAPI(){
        //Obtains restaurant object
        ConnectAPI connectAPI = new ConnectAPI();
        Rest_Data restaurant = connectAPI.getRestaurant(id);

        //Set variables
        setTitle(restaurant.getName());
        setScore(String.valueOf(restaurant.getScore()));
        setTime(restaurant.getScheduleTime());

        ArrayList<String> days = restaurant.getSchedule();
        String schedule = "";
        schedule = days.get(0);
        for(int i = 1 ; i < days.size() ; ++i){
            schedule += ", " + days.get(i);
        }
        setSchedule(schedule);

        setImage(restaurant.getAllBitmaps());

    }

    private void setTitle(String title){
        TextView titleView = (TextView)findViewById(R.id.restaurantTitle);
        titleView.setText(title);
    }

    private void setSchedule(String description){
        TextView scheduleView = (TextView)findViewById(R.id.restaurantSchedule);
        scheduleView.setText(description);
    }

    private void setImage(ArrayList<Bitmap> bitmaps){

        for (int i = 0 ; i < bitmaps.size() ; ++i){
            ImageView image = new ImageView(this);
            image.setImageBitmap(bitmaps.get(i));
            LinearLayout layout = findViewById(R.id.imagesLayout);
            layout.addView(image);
        }

    }

    private void setScore(String score){
        TextView scoreView = (TextView)findViewById(R.id.restaurantScore);
        scoreView.setText(score);
    }
    private void setTime(String time){
        TextView scoreView = (TextView)findViewById(R.id.restaurantTime);
        scoreView.setText(time);
    }

    private void setStars(int starQuantity){
        --starQuantity;
        LinearLayout stars = findViewById(R.id.stars);
        if(starQuantity != -1){
            boolean indicator = true;
            for(int i = 0 ; i < 5 ; ++i){
                ImageView star = (ImageView) stars.getChildAt(i);

                if(indicator) {
                    star.setImageResource(android.R.drawable.btn_star_big_on);
                    if(i == starQuantity) indicator = false;

                } else {
                    star.setImageResource(android.R.drawable.btn_star_big_off);
                }
            }
        } else {
            for(int i = 0 ; i < 5 ; ++i){
                ImageView star = (ImageView) stars.getChildAt(i);
                star.setImageResource(android.R.drawable.btn_star_big_off);
            }
        }
    }


    //-----Methods to connect with API (Get and Set)
    private void updateComments(){
        final ArrayList<String> comments = new ArrayList<String>();
        ConnectAPI connectAPI = new ConnectAPI();
        ArrayList<String> com = connectAPI.getComments(id);
        LinearLayout layout = findViewById(R.id.commentLayout);
        layout.removeAllViewsInLayout();
        for (int i = 0 ; i < com.size() ; ++i){
            TextView textView = new TextView(this);
            textView.setText(com.get(i));
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(0x5FFFFFFF);

            gradientDrawable.setStroke(1, 0xFF000000);
            textView.setBackground(gradientDrawable);
            textView.setHeight(200);

            layout.addView(textView);
        }
/*
        for(int i = 0 ; i < com.size() ; ++ i){
            comments.add(com.get(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, comments);
        commentsListView.setAdapter(adapter);*/
    }

    private boolean uploadComment(String newComment){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String session = sharedPreferences.getString("session", null);

        ConnectAPI connectAPI = new ConnectAPI();
        connectAPI.uploadComment(id, newComment, session);

        updateComments();
        return true;
    }

    private boolean uploadStars(int quantity){
        ConnectAPI connectAPI = new ConnectAPI();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String session = sharedPreferences.getString("session", null);

        connectAPI.uploadStars(id, String.valueOf(quantity), session);
        //Se suben las estrellas a la api
        return true;
    }


    //------Control View Methods
    //Rates th star and uploads it
    public void rateStar(View view){
        LinearLayout stars = findViewById(R.id.stars);
        boolean indicator = true;

        ImageView star = (ImageView) stars.getChildAt(0);
        int starQuantity = 1;
        while(star != view){
            star = (ImageView) stars.getChildAt(starQuantity);
            ++starQuantity;
        }

        setStars(starQuantity);
        uploadStars(starQuantity);
    }

    //Get comment. Calls uploadComment. Closes keyboard.
    public void postComment(View view){
        EditText comentario = (EditText)findViewById(R.id.comment);
        uploadComment(comentario.getText().toString());

        comentario.setText("");
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        setInformation();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
