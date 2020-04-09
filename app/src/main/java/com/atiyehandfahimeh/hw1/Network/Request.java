package com.atiyehandfahimeh.hw1.Network;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String url;
    private HashMap<String, String> queries;


    public Request(String baseUrl, HashMap<String, String> queries) {
        this.queries = queries;
        StringBuilder baseUrlBuilder = new StringBuilder(baseUrl + "?");
        for (Map.Entry query : queries.entrySet()) {
            String key = (String)query.getKey();
            String value = (String) query.getValue();
            baseUrlBuilder.append(key).append("=").append(value).append("&");
        }
        this.url = baseUrlBuilder.toString();
    }

    public void send(Context context, Handler handler){
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Message mUi = Message.obtain();
                        mUi.arg1 = State.SUCCESS.getValue();
                        mUi.obj = response;
                        handler.sendMessage(mUi);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message msgUI = Message.obtain();
                        msgUI.arg1 = State.FAIL.getValue();
                        msgUI.obj = error;
                        handler.sendMessage(msgUI);
                    }
                });
        queue.add(jsonObjectRequest);
    }


}
