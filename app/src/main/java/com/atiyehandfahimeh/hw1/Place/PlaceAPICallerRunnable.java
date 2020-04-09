package com.atiyehandfahimeh.hw1.Place;

import android.content.Context;
import android.os.Handler;

import com.atiyehandfahimeh.hw1.Network.Request;

import java.util.HashMap;

public class PlaceAPICallerRunnable implements Runnable {
    private String searchText;
    private Context mainContext;
    private Handler placeHandler;
    private final String baseUrl = "https://api.mapbox.com/geocoding/v5/mapbox.places/";
    private final String accessToken =
            "pk.eyJ1IjoiZmFoaW0tdCIsImEiOiJjazhtY3dwc3AwMHJzM2ducW9hcGF3N2cwIn0.2BsWu6j2i863YTGzlFikcQ";

    public PlaceAPICallerRunnable(Context mainContext, Handler placeHandler, String searchText) {
        this.searchText = searchText;
        this.mainContext = mainContext;
        this.placeHandler = placeHandler;
    }

    @Override
    public void run() {
        HashMap<String, String> queries = new HashMap<String, String>();
        queries.put("access_token", accessToken);
        Request request = new Request(baseUrl + this.searchText + ".json", queries);
        request.send(this.mainContext, placeHandler);
    }

}


