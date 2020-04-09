package com.atiyehandfahimeh.hw1.Weather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.atiyehandfahimeh.hw1.DataBase.DataBaseClient;
import com.atiyehandfahimeh.hw1.DataBase.Weathers;
import com.atiyehandfahimeh.hw1.Models.DayWeather;
import com.atiyehandfahimeh.hw1.Network.InternetConnection;
import com.atiyehandfahimeh.hw1.Network.State;
import com.atiyehandfahimeh.hw1.Place.SearchPlaceActivity;
import com.atiyehandfahimeh.hw1.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WeatherDisplayActivity extends AppCompatActivity {
    private RecyclerView weatherRecyclerView;
    private RecyclerView.Adapter weatherAdapter;
    private RecyclerView.LayoutManager weatherLayoutManager;
    private ProgressBar weatherProgressBar;
    private TextView cityName;
    private TextView countryName;
    private TextView lastUpdate;
    private Double longitude;
    private Double latitude;
    ArrayList <DayWeather> dayWeathers;
    private WeatherParseResponse weatherData = new WeatherParseResponse();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_display);
        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);
        getViewFromXML();

//        Thread thread = new Thread(new CheckNetworkRunnable(checkNetworkHandler, this));
//        thread.start();
        InternetConnection connection = new InternetConnection(this);
        if(connection.isConnected()){
            fetchFromServer();
        }
        else {
            fetchFromDataBase();
        }
//        checkNetwork();
//        Log.i("weathermainactivity", "^^^^^^^^^^^^^^^^^^^^weathermainactivity pid: " + android.os.Process.myPid() +
//                " Tid: " + android.os.Process.myTid() +
//                " id: " + Thread.currentThread().getId());
    }

//    private void checkNetwork(){
//        InternetConnection connection = new InternetConnection(this);
//        if(connection.isConnected()){
//            Log.i("DataBase", "ffffffkkk");
//            fetchFromServer();
//        } else{
//            Toast.makeText(this, ":((((((", Toast.LENGTH_LONG);
//            Log.i("DataBase", "ffffffffff");
////            fetchFromDataBase();
//        }
//
//    }

    private void fetchFromServer(){
        weatherProgressBar.setVisibility(View.VISIBLE);
        Handler weatherHandler = handler();
        Thread apiCallerThread = new Thread(new WeatherAPICallerRunnable(this, weatherHandler, latitude, longitude)); //I pass Runnable object in thread so that the code inside the run() method
        //of Runnable object gets executed when I start my thread here. But the code executes in new thread
        apiCallerThread.start();
    }

    private void fetchFromDataBase(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                List<Weathers> weatherDataBaseList = DataBaseClient.getInstance(WeatherDisplayActivity.this)
                        .getAppDatabase().weatherDao().getAll();
                dayWeathers = new ArrayList<>();
                for (Weathers weather: weatherDataBaseList) {
                    Log.i("VVVVVVV", weather.getWeather());
                    DayWeather dayWeather = new DayWeather(weather.getDate(), weather.getWeather(), weather.getPhotoCode(),
                            weather.getMaxTemp(), weather.getMinTemp(), weather.getAvgTemp());
                    dayWeathers.add(dayWeather);
                }
                // refreshing recycler view
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        weatherAdapter = new WeatherAdapter(WeatherDisplayActivity.this, dayWeathers);
                        weatherAdapter.notifyDataSetChanged();
                        weatherRecyclerView.setLayoutManager(weatherLayoutManager);
                        weatherRecyclerView.setAdapter(weatherAdapter);
                    }
                });
            }
        });
        thread.start();
    }

    private Handler handler(){
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 == State.SUCCESS.getValue()) {

                    weatherData.parseSuccessResponse((JSONObject) msg.obj);
                    saveData();
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

    private void saveData() {


        class SaveData extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a dayWeather

                for (int i = 0; i < weatherData.getDayData().size(); i++) {
                    DayWeather dayWeather = weatherData.getDayData().get(i) ;
                    Weathers weathers = new Weathers();
                    weathers.setDate(dayWeather.getDate());
                    weathers.setWeather(dayWeather.getWeather());
                    weathers.setMaxTemp(dayWeather.getMaxTemp());
                    weathers.setMinTemp(dayWeather.getMinTemp());
                    weathers.setAvgTemp(dayWeather.getAvgTemp());
                    weathers.setPhotoCode(dayWeather.getPhotoCode());
                    DataBaseClient.getInstance(getApplicationContext()).getAppDatabase().
                            weatherDao().insert(weathers);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveData st = new SaveData();
        st.execute();
    }

}

class CheckNetworkRunnable implements Runnable{
    private Handler handler;
    private Context context;
    public CheckNetworkRunnable(Handler handler, Context context) {
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void run() {
        Message mUi = Message.obtain();
        InternetConnection connection = new InternetConnection(context);

        if (connection.isConnected()){

        }
        else {

        }
        handler.sendMessage(mUi);
    }
}
