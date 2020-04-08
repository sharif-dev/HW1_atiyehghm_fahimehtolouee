package com.atiyehandfahimeh.hw1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherDisplayActivity extends AppCompatActivity {
    private RecyclerView weatherRecyclerView;
    private RecyclerView.Adapter weatherAdapter;
    private RecyclerView.LayoutManager wetherLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_display);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        //i should get x and y here
        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        WeatherAPIRequest(35.696, 51.401);
        Log.i("weathermainactivity", "^^^^^^^^^^^^^^^^^^^^weathermainactivity pid: " + android.os.Process.myPid() +
                " Tid: " + android.os.Process.myTid() +
                " id: " + Thread.currentThread().getId());
    }

    //creating a string  request
    private void WeatherAPIRequest(Double latitude, Double longitude) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.weatherapi.com/v1/forecast.json?q=" + latitude.toString() +
                ","+ longitude.toString() +"&key=280430d7b3264e7fabd52834200604&days=7";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        processForecastObject(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        Log.i("weathermainactivity", "*****************weathermainactivity pid: " + android.os.Process.myPid() +
                " Tid: " + android.os.Process.myTid() +
                " id: " + Thread.currentThread().getId());
        queue.add(jsonObjectRequest);
    }

    private void processForecastObject(JSONObject response) {
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
                dayWeather.setMaxTemp(forecastday.getJSONObject(i).getJSONObject("day").getDouble("maxtemp_c"));
                dayWeather.setMinTemp(forecastday.getJSONObject(i).getJSONObject("day").getDouble("mintemp_c"));
                dayWeather.setAvgTemp(forecastday.getJSONObject(i).getJSONObject("day").getDouble("avgtemp_c"));
                dayWeather.setWeather(forecastday.getJSONObject(i).getJSONObject("day").getJSONObject("condition").getString("text"));
                dayWeather.setPhotoCode(forecastday.getJSONObject(i).getJSONObject("day").getJSONObject("condition").getInt("code"));
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
