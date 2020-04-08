package com.atiyehandfahimeh.hw1;
import android.content.Context;

import android.os.Message;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import android.os.Handler;


public class WeatherAPICallerRunnable implements Runnable {
    private Handler aPICallHandler;
    private Double latitude;
    private Double longitude;
    private Context mainContext;

    public WeatherAPICallerRunnable(Context mainContext, Handler aPICallHandler, Double latitude, Double longitude) {
        this.mainContext = mainContext;
        this.aPICallHandler = aPICallHandler;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void run() {
        RequestQueue queue = Volley.newRequestQueue(this.mainContext);
        String url = "http://api.weatherapi.com/v1/forecast.json?q=" + latitude.toString() +
                ","+ longitude.toString() +"&key=280430d7b3264e7fabd52834200604&days=7";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Message mUi = Message.obtain();
                        mUi.obj = response;
                        aPICallHandler.sendMessage(mUi);
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
}
