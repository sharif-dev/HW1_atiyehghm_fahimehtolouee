package com.atiyehandfahimeh.hw1;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaceAPICallerRunnable implements Runnable {
    private String searchtext;
    private Context maincontext;
    private ArrayList<ListItem> listItems;
    Handler placeApiHandler;

    public PlaceAPICallerRunnable(String searchtext, Context maincontext, Handler placeApiHandler) {
        this.searchtext = searchtext;
        this.maincontext = maincontext;
        this.placeApiHandler = placeApiHandler;
    }

    @Override
    public void run() {
        Log.i("mainactivity", "************************mainactivity pid: " + android.os.Process.myPid() +
                " Tid: " + android.os.Process.myTid() +
                " id: " + Thread.currentThread().getId());
        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + this.searchtext +
                ".json?" +
                "access_token=pk.eyJ1IjoiZmFoaW0tdCIsImEiOiJjazhtY3dwc3AwMHJzM2ducW9hcGF3N2cwIn0.2BsWu6j2i863YTGzlFikcQ";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listItems = new ArrayList<>();
                        try {
                            JSONArray places = response.optJSONArray(DataKeys.getFEATURES());
                            for (int i = 0; i < places.length(); i++) {
                                JSONObject currentPlace = places.optJSONObject(i);
                                String id = currentPlace.optString(DataKeys.getID());
                                String name = currentPlace.optString(DataKeys.getPlaceName());
                                double centerX = currentPlace.optJSONArray(DataKeys.getCENTER()).getDouble(0);
                                double centerY = currentPlace.optJSONArray(DataKeys.getCENTER()).getDouble(1);
                                listItems.add(new ListItem(id, name, centerX, centerY));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message msgUI = Message.obtain();
                        msgUI.arg1 = 1;//means its a response message
                        msgUI.obj = listItems;
                        placeApiHandler.sendMessage(msgUI);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message msgUI = Message.obtain();
                msgUI.arg1 = 2;//means its error message
                msgUI.obj = error;
                placeApiHandler.sendMessage(msgUI);
            }
        });
        Log.i("mainactivity", "************************mainactivity pid: " + android.os.Process.myPid() +
                " Tid: " + android.os.Process.myTid() +
                " id: " + Thread.currentThread().getId());
        Volley.newRequestQueue(this.maincontext).add(jsonObjectRequest);
    }

}


