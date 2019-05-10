package com.example.mobile_rest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String email = sharedPreferences.getString("email", null);
        ConnectAPI connectAPI = new ConnectAPI();
        int score = connectAPI.getStarFromEmail(id, email);

        setStars(score);
    }

    private void downloadAPI(){
        //Obtains restaurant object
        ConnectAPI connectAPI = new ConnectAPI();
        Rest_Data restaurant = connectAPI.getRestaurant(id);

        //Set variables
        setTitle(restaurant.getName());
        setScore(String.valueOf(restaurant.getScore()));
        setTime(restaurant.getScheduleTime());
        setContacts(restaurant.getNameContact(), restaurant.getValueContact());
        setType(restaurant.getType());
        setCost(restaurant.getPrice());

        ArrayList<String> days = restaurant.getSchedule();
        String schedule = "";
        schedule = days.get(0);
        for(int i = 1 ; i < days.size() ; ++i){
            schedule += ", " + days.get(i);
        }
        setSchedule(schedule);

        setImage(restaurant.getAllBitmaps());

    }

    private void setType(String type){
        TextView textView = findViewById(R.id.restaurantType);
        textView.setText(type);
    }

    private void setCost(String cost){
        TextView textView = findViewById(R.id.restaurantPrice);
        textView.setText(cost);
    }

    private void setContacts(ArrayList<String> name, ArrayList<String> value){
        LinearLayout linearLayout = findViewById(R.id.infoContactsLayout);
        for(int i = 0; i < name.size(); ++i){
            TextView textView = new TextView(this);
            textView.setText(name.get(i) + ": " + value.get(i));
            linearLayout.addView(textView);
        }
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

    public void getImage(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //sin permiso
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 782);
        } else {
            //con permiso
            cargarImagen();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions , int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 782) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Dieron permiso
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    cargarImagen();
                }
            }
        }
    }

    public void cargarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent,123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case 123:
                    Uri imageUri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);

                    cursor.close();

                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String session = sharedPreferences.getString("session", null);

                    ConnectAPI connectAPI = new ConnectAPI();
                    connectAPI.uploadPhoto(session, id, bitmap);

                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    if(width > 1080 || height > 720){
                        width = (int) (0.4 * width);
                        height = (int) (0.4 * height);
                        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                    }

                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(bitmap);

                    LinearLayout linearLayout = findViewById(R.id.imagesLayout);
                    linearLayout.addView(imageView);

                    break;
            }
        }
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
        //imagen app
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

    }
}
