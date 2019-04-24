package com.example.mobile_rest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class RestaurantInfo extends AppCompatActivity {

    ListView commentsListView;
    final ArrayList<String> comments = new ArrayList<String>();

    String userName = "Prueba"; // TODO Hacer esto en cache despues de login


    private void download(){

        String description = "Esto es la descripción del restaurante. " +
                " Es una muy buena descripcion que describe con palabras" +
                " un restaurante. Wow, que buena descripción.";

        setComments();
        setDescription(description);
        setImage();
        setStars(1);
    }

    private void setDescription(String description){
        TextView descriptionView = (TextView)findViewById(R.id.restaurantDescription);
        descriptionView.setText(description);
    }

    private void setImage(){
        findViewById(R.id.profileImg).setBackgroundResource(R.mipmap.ic_launcher);
    }

    private void setStars(int starQuantity){
        LinearLayout stars = findViewById(R.id.stars);
        boolean indicator = true;

        for(int i = 0 ; i < 5 ; ++i){
            ImageView star = (ImageView) stars.getChildAt(i);

            if(indicator) {
                star.setImageResource(android.R.drawable.btn_star_big_on);
                if(i == starQuantity-1) indicator = false;

            } else {
                star.setImageResource(android.R.drawable.btn_star_big_off);
            }
        }
    }

    private void setComments(){
        commentsListView = findViewById(R.id.comments);

        //Se agregar comentarios a comments

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, comments);
        commentsListView.setAdapter(adapter);
    }


    //Set a la api
    private boolean uploadComment(String newComment){
        comments.add( userName + ": " + newComment);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, comments);
        commentsListView.setAdapter(adapter);

        //subir comentario a API
        return true;
    }

    private boolean uploadStars(int quantity){
        //Se suben las estrellas a la api
        return true;
    }

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
    }

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
        //envio ID de marcador
        String id = intent.getStringExtra("ID");


    }
}
