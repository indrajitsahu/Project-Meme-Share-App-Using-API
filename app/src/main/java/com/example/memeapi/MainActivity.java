package com.example.memeapi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ImageView memeImageView;
    TextView shareButton, nextButton;
    ProgressBar progressBar;
    String currentImageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        memeImageView = findViewById(R.id.memeImageView);
        shareButton = findViewById(R.id.shareButton);
        nextButton = findViewById(R.id.nextButton);
        progressBar = findViewById(R.id.progressBar);

        getSupportActionBar().hide();
        loadMeme();
    }

    private void loadMeme(){

        progressBar.setVisibility(ProgressBar.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://meme-api.herokuapp.com/gimme";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
                    public void onResponse(JSONObject response) {
                        try {
                            currentImageUrl = response.getString("url");
//                            Glide.with(MainActivity.this).load(currentImageUrl).into(memeImageView);
//                            progressBar.setVisibility(ProgressBar.GONE);

                            Glide.with(MainActivity.this).load(currentImageUrl).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                    progressBar.setVisibility(ProgressBar.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(ProgressBar.GONE);
                                    return false;
                                }
                            }).into(memeImageView);

                        }catch (Exception e) {e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
                Toast.makeText(MainActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void shareMeme(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, currentImageUrl);
        startActivity(intent);
    }

    public void nextMeme(View view) {
        loadMeme();
    }
}