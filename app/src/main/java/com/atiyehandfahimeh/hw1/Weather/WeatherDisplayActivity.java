package com.atiyehandfahimeh.hw1.Weather;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.atiyehandfahimeh.hw1.Network.State;
import com.atiyehandfahimeh.hw1.R;

import org.json.JSONObject;


public class WeatherDisplayActivity extends AppCompatActivity {
    private RecyclerView weatherRecyclerView;
    private RecyclerView.Adapter weatherAdapter;
    private RecyclerView.LayoutManager weatherLayoutManager;
    private ProgressBar weatherProgressBar;
    private TextView cityName;
    private TextView countryName;
    private TextView lastUpdate;
    private WeatherParseResponse weatherData = new WeatherParseResponse();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_display);
        Intent intent = getIntent();
        Double latitude = intent.getDoubleExtra("latitude", 0);
        Double longitude = intent.getDoubleExtra("longitude", 0);
        getViewFromXML();
        weatherProgressBar.setVisibility(View.VISIBLE);
        Handler weatherHandler = handler();
        Thread apiCallerThread = new Thread(new WeatherAPICallerRunnable(this, weatherHandler, latitude, longitude)); //I pass Runnable object in thread so that the code inside the run() method
        //of Runnable object gets executed when I start my thread here. But the code executes in new thread
        apiCallerThread.start();
//        Log.i("weathermainactivity", "^^^^^^^^^^^^^^^^^^^^weathermainactivity pid: " + android.os.Process.myPid() +
//                " Tid: " + android.os.Process.myTid() +
//                " id: " + Thread.currentThread().getId());
    }


    private Handler handler(){
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 == State.SUCCESS.getValue()) {

                    weatherData.parseSuccessResponse((JSONObject) msg.obj);
                    mainController();

                }else if (msg.arg1 == State.FAIL.getValue()){

                    weatherData.parseFailureResponse((VolleyError) msg.obj);
                    secondController();
                }
            }
        };
    }
    private void getViewFromXML(){
        weatherProgressBar = findViewById(R.id.WeatherprogressBar);
        cityName = findViewById(R.id.cityname);
        countryName = findViewById(R.id.countryname);
        lastUpdate = findViewById(R.id.lastupdate);
        weatherRecyclerView = findViewById(R.id.weatherlist);
    }

    private void mainController() {
        weatherProgressBar.setVisibility(View.INVISIBLE);

        cityName.setText(weatherData.getCity());
        countryName.setText(weatherData.getCountry());
        lastUpdate.setText(R.string.last_update + weatherData.getLastUpdate());

        weatherRecyclerView.setHasFixedSize(true);
        weatherLayoutManager = new LinearLayoutManager(this);
        weatherAdapter = new WeatherAdapter(this, weatherData.getDayData());
        weatherRecyclerView.setLayoutManager(weatherLayoutManager);
        weatherRecyclerView.setAdapter(weatherAdapter);
    }

    private void secondController(){
        weatherProgressBar.setVisibility(View.INVISIBLE);

        Toast.makeText(
                getApplicationContext(), weatherData.getErrorMessage(), Toast.LENGTH_LONG
        ).show();
    }

}
