package com.atiyehandfahimeh.hw1.Place;

import androidx.arch.core.util.Function;

import com.android.volley.VolleyError;
import com.atiyehandfahimeh.hw1.Constants.PlaceDataKeys;
import com.atiyehandfahimeh.hw1.Models.Place;
import com.atiyehandfahimeh.hw1.Network.HandleError;
import com.atiyehandfahimeh.hw1.Network.ParseResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaceParseResponse implements ParseResponse {
    private ArrayList<Place> placeList;
    private String errorMessage;
    @Override
    public void parseSuccessResponse(JSONObject response) {
        placeList = new ArrayList<Place>();
        try {
            JSONArray places = response.optJSONArray(PlaceDataKeys.getFEATURES());
            for (int i = 0; i < places.length(); i++) {
                JSONObject currentPlace = places.optJSONObject(i);
                String id = currentPlace.optString(PlaceDataKeys.getID());
                String name = currentPlace.optString(PlaceDataKeys.getPlaceName());
                double centerX = currentPlace.optJSONArray(PlaceDataKeys.getCENTER()).getDouble(0);
                double centerY = currentPlace.optJSONArray(PlaceDataKeys.getCENTER()).getDouble(1);
                placeList.add(new Place(id, name, centerX, centerY));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseFailureResponse(VolleyError error) {
        HandleError handleError = new HandleError(error);
        Function<JSONObject, String> parseError = data->
                data.optString(PlaceDataKeys.getMESSAGE());
        errorMessage = handleError.handleError(parseError);
    }

    public ArrayList<Place> getPlaceList() {
        return placeList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
