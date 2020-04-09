package com.atiyehandfahimeh.hw1.Weather;

import android.content.Context;
import android.os.Handler;

import com.atiyehandfahimeh.hw1.Network.Request;

import java.util.HashMap;


public class WeatherAPICallerRunnable implements Runnable {
    private Handler weatherHandler;
    private Double latitude;
    private Double longitude;
    private Context mainContext;
    private final String baseUrl = "http://api.weatherapi.com/v1/forecast.json";
    private final String accessToken = "280430d7b3264e7fabd52834200604";
    private final String days = "7";

    public WeatherAPICallerRunnable(Context mainContext, Handler weatherHandler, Double latitude, Double longitude) {
        this.mainContext = mainContext;
        this.weatherHandler = weatherHandler;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void run() {
        HashMap<String, String> queries = new HashMap<String, String>();
        queries.put("q", longitude.toString() + "," + latitude.toString() );
        queries.put("key", accessToken);
        queries.put("days", days );
        Request request = new Request(baseUrl, queries);
        request.send(this.mainContext, weatherHandler);

//        Log.i("weathermainactivity", "*****************weathermainactivity pid: " + android.os.Process.myPid() +
//                " Tid: " + android.os.Process.myTid() +
//                " id: " + Thread.currentThread().getId());
    }
}
