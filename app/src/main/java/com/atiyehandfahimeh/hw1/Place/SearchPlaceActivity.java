package com.atiyehandfahimeh.hw1.Place;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.atiyehandfahimeh.hw1.Weather.WeatherDisplayActivity;
import com.atiyehandfahimeh.hw1.Models.Place;
import com.atiyehandfahimeh.hw1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SearchPlaceActivity extends AppCompatActivity implements PlaceAdapter.OnItemClickListener {
    private static final String EXTRA_LONGITUDE = "longitude";
    private static final String EXTRA_LATITUDE = "latitude";
    private EditText searchBox;
    private ImageView searchButton;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ArrayList<Place> places;
    private PlaceAdapter placeAdapter;
    private Handler placeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViewFromXML();
        controller();
    }

    private void APICall(String text) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        placeHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 == 1) {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    places = (ArrayList<Place>) msg.obj;
                    placeAdapter.setData(places);
                    placeAdapter.notifyDataSetChanged();
                } else if (msg.arg1 == 2) {
                    VolleyError error = (VolleyError) msg.obj;
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);
                        String message = data.optString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        Thread thread = new Thread(new PlaceAPICallerRunnable(text, this, placeHandler));
        thread.start();
//        Log.i("mainactivity", "^^^^^^^^^^^^^^^^^^^^mainactivity pid: " + android.os.Process.myPid() +
//                " Tid: " + android.os.Process.myTid() +
//                " id: " + Thread.currentThread().getId());
    }

    private void controller() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APICall(searchBox.getText().toString());
            }
        });
        searchBox.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH
                                || actionId == EditorInfo.IME_ACTION_DONE
                                || event.getAction() == KeyEvent.ACTION_DOWN
                                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            APICall(searchBox.getText().toString());
                            return true;
                        }
                        return false;
                    }
                }
        );
        placeAdapter = new PlaceAdapter(this , new ArrayList<Place>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(placeAdapter);
        placeAdapter.setOnItemClickListener(SearchPlaceActivity.this);
    }

    private void getViewFromXML() {
        searchBox = findViewById(R.id.searchBox);
        searchButton = findViewById(R.id.searchButton);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.listItem);
    }

    @Override
    public void onItemClick(int position) {
        Intent showWeatherIntent = new Intent(this, WeatherDisplayActivity.class);
        Place clickedItem = places.get(position);

        showWeatherIntent.putExtra(EXTRA_LONGITUDE, clickedItem.getCenterX());
        showWeatherIntent.putExtra(EXTRA_LATITUDE, clickedItem.getCenterY());
        startActivity(showWeatherIntent);
    }
}
