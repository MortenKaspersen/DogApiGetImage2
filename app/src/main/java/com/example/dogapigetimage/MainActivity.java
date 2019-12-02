package com.example.dogapigetimage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Button mButton;
    private TextView mJsonTextView;
    private String base = "https://dog.ceo/api/breeds/image/random";
    private RequestQueue queue;
    private ArrayList<Dog> dogs;
    private int size;
    ProgressDialog pd;
    String FinalImageURL = "https:\\/\\/images.dog.ceo\\/breeds\\/poodle-miniature\\/n02113712_10433.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.imgDog);
        mJsonTextView = (TextView) findViewById(R.id.txtJson);

        final String dog = FinalImageURL;
        Picasso.get().load(dog).into(mImageView);

        //Inspiration fra: https://guides.codepath.com/android/Displaying-Images-with-the-Picasso-Library
        //Picasso.get().load("https:\\/\\/images.dog.ceo\\/breeds\\/poodle-miniature\\/n02113712_10433.jpg").into(mImageView);

        mButton = (Button) findViewById(R.id.btnNext);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonTask().execute("https://dog.ceo/api/breeds/image/random");

                //String dog = FinalImageURL;
                //Picasso.get().load(dog).into(mImageView);

                Toast.makeText(MainActivity.this,"" + dog,Toast.LENGTH_SHORT).show();
                //Picasso.get().load(String.valueOf(dog)).into(mImageView);

            }
        });
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            mJsonTextView.setText(result);
            //Toast.makeText(MainActivity.this,"" + result,Toast.LENGTH_SHORT).show();

            int indexOfLast = result.lastIndexOf(",");
            String newString = result;
            if (indexOfLast >= 0){
                newString = result.substring(0, indexOfLast);
            }

            String imageURL = newString.substring(11);

            //String finalImagePath = imageURL.substring(0, imageURL.length() -1);

            FinalImageURL = imageURL;

            Picasso.get().load(imageURL).into(mImageView);

            //Toast.makeText(MainActivity.this,"" + imageURL,Toast.LENGTH_SHORT).show();
        }
    }


}


