package com.atiyehandfahimeh.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDisplayActivity extends AppCompatActivity {

    ListAdapter adapter;
    ListView weatherView ;
    List dayList = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_display);

        weatherView = findViewById(R.id.weatherlist);
        dayList.add("sat");
        dayList.add("sun");
        dayList.add("mon");
        dayList.add("tue");
        dayList.add("wed");
        dayList.add("thur");
        dayList.add("fri");

        adapter = new ArrayAdapter(WeatherDisplayActivity.this, android.R.layout.simple_list_item_1, dayList);
        weatherView.setAdapter(adapter);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        //i should get x and y here
        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        WeatherAPIRequest(35.696, 51.401);
    }

    //creating a string  request
    private void WeatherAPIRequest(Double latitude, Double longitude) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.weatherapi.com/v1/forecast.json?q=" + latitude.toString() +
                ","+ longitude.toString() +"&key=280430d7b3264e7fabd52834200604&days=7";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null,  new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        processForecastObject(response);
                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TextView textView = findViewById(R.id.requestviewer);
                        //textView.setText("not worked");
                    }
                });
        queue.add(jsonObjectRequest);
    }

    private void processForecastObject(JSONObject response) {
        System.out.println("in processing");
        String city = null;
        try {
            JSONObject location = response.getJSONObject("location");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //TextView textView = findViewById(R.id.requestviewer);
        //textView.setText(city);
    }



}
