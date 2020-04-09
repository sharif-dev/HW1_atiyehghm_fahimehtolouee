package com.atiyehandfahimeh.hw1.Place;

import android.content.Context;
import android.os.Handler;
import android.os.Message;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atiyehandfahimeh.hw1.Constants.PlaceDataKeys;
import com.atiyehandfahimeh.hw1.Models.Place;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaceAPICallerRunnable implements Runnable {
    private String searchtext;
    private Context maincontext;
    private ArrayList<Place> places;
    private Handler placeHandler;

    public PlaceAPICallerRunnable(String searchText, Context mainContext, Handler placeHandler) {
        this.searchtext = searchText;
        this.maincontext = mainContext;
        this.placeHandler = placeHandler;
    }

    @Override
    public void run() {
//        Log.i("mainactivity", "************************mainactivity pid: " + android.os.Process.myPid() +
//                " Tid: " + android.os.Process.myTid() +
//                " id: " + Thread.currentThread().getId());
        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + this.searchtext +
                ".json?" +
                "access_token=pk.eyJ1IjoiZmFoaW0tdCIsImEiOiJjazhtY3dwc3AwMHJzM2ducW9hcGF3N2cwIn0.2BsWu6j2i863YTGzlFikcQ";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        places = new ArrayList<>();
                        try {
                            JSONArray places = response.optJSONArray(PlaceDataKeys.getFEATURES());
                            for (int i = 0; i < places.length(); i++) {
                                JSONObject currentPlace = places.optJSONObject(i);
                                String id = currentPlace.optString(PlaceDataKeys.getID());
                                String name = currentPlace.optString(PlaceDataKeys.getPlaceName());
                                double centerX = currentPlace.optJSONArray(PlaceDataKeys.getCENTER()).getDouble(0);
                                double centerY = currentPlace.optJSONArray(PlaceDataKeys.getCENTER()).getDouble(1);
                                PlaceAPICallerRunnable.this.places.add(new Place(id, name, centerX, centerY));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message msgUI = Message.obtain();
                        msgUI.arg1 = 1;//means its a response message
                        msgUI.obj = places;
                        placeHandler.sendMessage(msgUI);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message msgUI = Message.obtain();
                msgUI.arg1 = 2;//means its error message
                msgUI.obj = error;
                placeHandler.sendMessage(msgUI);
            }
        });
//        Log.i("mainactivity", "************************mainactivity pid: " + android.os.Process.myPid() +
//                " Tid: " + android.os.Process.myTid() +
//                " id: " + Thread.currentThread().getId());
        Volley.newRequestQueue(this.maincontext).add(jsonObjectRequest);
    }

}


