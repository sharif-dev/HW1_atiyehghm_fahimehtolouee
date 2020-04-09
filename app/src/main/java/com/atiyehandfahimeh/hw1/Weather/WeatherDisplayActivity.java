package com.atiyehandfahimeh.hw1.Weather;

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
import com.atiyehandfahimeh.hw1.DataBase.Model.City;
import com.atiyehandfahimeh.hw1.DataBase.DataBaseClient;
import com.atiyehandfahimeh.hw1.DataBase.Model.Climate;
import com.atiyehandfahimeh.hw1.Models.CityInfo;
import com.atiyehandfahimeh.hw1.Models.DayClimate;
import com.atiyehandfahimeh.hw1.Network.InternetConnection;
import com.atiyehandfahimeh.hw1.Network.State;
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
    CityInfo cityInformation;
    private WeatherParseResponse cityData = new WeatherParseResponse();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_display);
        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);
        getViewFromXML();

        InternetConnection connection = new InternetConnection(this);
        if(connection.isConnected()){
            fetchFromServer();
        }
        else {
            fetchFromDataBase();
        }
    }


    private void fetchFromServer(){
        weatherProgressBar.setVisibility(View.VISIBLE);
        Handler weatherHandler = handler();
        Thread apiCallerThread = new Thread(new WeatherAPICallerRunnable(this,
                weatherHandler, latitude, longitude));
        apiCallerThread.start();
    }

    private void fetchFromDataBase(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<City> citiesDataBaseList = DataBaseClient.getInstance(WeatherDisplayActivity.this)
                        .getAppDatabase().cityDao().getAll();
                City lastCity = citiesDataBaseList.get(citiesDataBaseList.size() - 1);
                List<Climate> climatesDataBaseList = DataBaseClient.getInstance(WeatherDisplayActivity.this)
                        .getAppDatabase().climateDao().getAll();

                ArrayList<DayClimate> weekClimate = new ArrayList<>();
                for(Climate climate: climatesDataBaseList){
                    DayClimate dayClimate = new DayClimate(
                            climate.getDate(), climate.getWeather(), climate.getPhotoCode(),
                            climate.getMaxTemp(), climate.getMinTemp(), climate.getAvgTemp()
                    );
                    weekClimate.add(dayClimate);
                }
                Log.i("eeeeee", weekClimate.get(2).getDate());
                cityInformation = new CityInfo(lastCity.getCity(), lastCity.getCountry(),
                        lastCity.getLastUpDate(), weekClimate);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        offlineController(cityInformation);
                    }
                });

//
            }
        });
        thread.start();
    }


    private void offlineController(CityInfo cityInfo) {
        Toast.makeText(getApplicationContext(), R.string.internet_connection, Toast.LENGTH_LONG).show();
        countryName.setText(cityInfo.getCountry());
        cityName.setText(cityInfo.getName());
        lastUpdate.setText(cityInfo.getLastUpdate());
        weatherRecyclerView.setHasFixedSize(true);
        weatherLayoutManager = new LinearLayoutManager(WeatherDisplayActivity.this);
        weatherAdapter = new WeatherAdapter(WeatherDisplayActivity.this, cityInfo.getWeekClimateInfo());
        weatherRecyclerView.setLayoutManager(weatherLayoutManager);
        weatherRecyclerView.setAdapter(weatherAdapter);
    }

    private Handler handler(){
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 == State.SUCCESS.getValue()) {

                    cityData.parseSuccessResponse((JSONObject) msg.obj);
                    saveData();
                    mainController();

                }else if (msg.arg1 == State.FAIL.getValue()){

                    cityData.parseFailureResponse((VolleyError) msg.obj);
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

        cityName.setText(cityData.getCityInfo().getName());
        countryName.setText(cityData.getCityInfo().getCountry());
        lastUpdate.setText(R.string.last_update + cityData.getCityInfo().getLastUpdate());

        weatherRecyclerView.setHasFixedSize(true);
        weatherLayoutManager = new LinearLayoutManager(WeatherDisplayActivity.this);
        weatherAdapter = new WeatherAdapter(this, cityData.getCityInfo().getWeekClimateInfo());
        weatherRecyclerView.setLayoutManager(weatherLayoutManager);
        weatherRecyclerView.setAdapter(weatherAdapter);
    }

    private void secondController(){
        weatherProgressBar.setVisibility(View.INVISIBLE);

        Toast.makeText(
                getApplicationContext(), cityData.getErrorMessage(), Toast.LENGTH_LONG
        ).show();
    }

    private void saveData() {


        class SaveData extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a dayWeather
                DataBaseClient.getInstance(getApplicationContext()).getAppDatabase().
                        climateDao().deleteAll();
                DataBaseClient.getInstance(getApplicationContext()).getAppDatabase().
                        cityDao().deleteAll();
                City city = new City(
                        cityData.getCityInfo().getName(),
                        cityData.getCityInfo().getCountry(),
                        cityData.getCityInfo().getLastUpdate()
                );
                DataBaseClient.getInstance(getApplicationContext()).getAppDatabase().
                        cityDao().insert(city);
                for (int i = 0; i < cityData.getCityInfo().getWeekClimateInfo().size(); i++) {
                    DayClimate dayClimate = cityData.getCityInfo().getWeekClimateInfo().get(i) ;
                    String date = dayClimate.getDate();
                    String weather = dayClimate.getWeather();
                    Double maxTemp = dayClimate.getMaxTemp();
                    Double minTemp = dayClimate.getMinTemp();
                    Double avgTemp = dayClimate.getAvgTemp();
                    int photoCode = dayClimate.getPhotoCode();
                    Log.i("rrrrr", Integer.toString(city.getId()));
                    Climate climate = new Climate(date, weather,photoCode,maxTemp,
                            minTemp,avgTemp);
                    DataBaseClient.getInstance(getApplicationContext()).getAppDatabase().
                            climateDao().insert(climate);
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
