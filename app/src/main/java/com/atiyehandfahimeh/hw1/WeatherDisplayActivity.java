package com.atiyehandfahimeh.hw1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class WeatherDisplayActivity extends AppCompatActivity {
    private RecyclerView weatherRecyclerView;
    private RecyclerView.Adapter weatherAdapter;
    private RecyclerView.LayoutManager wetherLayoutManager;
    private ProgressBar weatherProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_display);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        //i should get x and y here
        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        Double latitude = intent.getDoubleExtra("latitude", 0);
        Double longitude = intent.getDoubleExtra("longitude", 0);
        weatherProgressBar = findViewById(R.id.WeatherprogressBar);
        weatherProgressBar.setVisibility(View.VISIBLE);
        Handler weatherHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                processForecastObject((JSONObject) msg.obj);
            }
        };
        Thread apicallerThread = new Thread(new WeatherAPICallerRunnable(this, weatherHandler, latitude, longitude)); //I pass Runnable object in thread so that the code inside the run() method
        //of Runnable object gets executed when I start my thread here. But the code executes in new thread
        apicallerThread.start();
        Log.i("weathermainactivity", "^^^^^^^^^^^^^^^^^^^^weathermainactivity pid: " + android.os.Process.myPid() +
                " Tid: " + android.os.Process.myTid() +
                " id: " + Thread.currentThread().getId());
    }


    private void processForecastObject(JSONObject response) {
        weatherProgressBar.setVisibility(View.INVISIBLE);
        String city = null;
        String country = null;
        String lastupdate= null;

        try {
            //getting header information
            JSONObject location = response.getJSONObject("location");
            city = location.getString("name");
            country = location.getString("country");
            JSONObject current = response.getJSONObject("current");
            lastupdate = current.getString("last_updated");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView cityname = findViewById(R.id.cityname);
        cityname.setText(city);
        TextView countryname = findViewById(R.id.countryname);
        countryname.setText(country);
        TextView lastupdateview = findViewById(R.id.lastupdate);
        lastupdateview.setText("lastupdate: "+lastupdate);

        //processing the json array and retrieving important data
        try{
            JSONArray forecastday = response.getJSONObject("forecast").getJSONArray("forecastday");
            ArrayList<DayWeather> daydata = new ArrayList();
            for(int i  = 0; i < forecastday.length(); i++){
                DayWeather dayWeather = new DayWeather();
                dayWeather.setDate(forecastday.getJSONObject(i).getString("date"));
                dayWeather.setMaxtempc(forecastday.getJSONObject(i).getJSONObject("day").getDouble("maxtemp_c"));
                dayWeather.setMintempc(forecastday.getJSONObject(i).getJSONObject("day").getDouble("mintemp_c"));
                dayWeather.setAvgtempc(forecastday.getJSONObject(i).getJSONObject("day").getDouble("avgtemp_c"));
                dayWeather.setWeathertext(forecastday.getJSONObject(i).getJSONObject("day").getJSONObject("condition").getString("text"));
                dayWeather.setPhotocode(forecastday.getJSONObject(i).getJSONObject("day").getJSONObject("condition").getInt("code"));
                daydata.add(dayWeather);
            }

            weatherRecyclerView = findViewById(R.id.weatherlist);
            weatherRecyclerView.setHasFixedSize(true);
            wetherLayoutManager = new LinearLayoutManager(this);
            weatherAdapter = new WeatherAdapter(this, daydata);
            weatherRecyclerView.setLayoutManager(wetherLayoutManager);
            weatherRecyclerView.setAdapter(weatherAdapter);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }


}
