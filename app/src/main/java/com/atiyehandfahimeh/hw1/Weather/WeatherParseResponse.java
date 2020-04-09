package com.atiyehandfahimeh.hw1.Weather;

import androidx.arch.core.util.Function;

import com.android.volley.VolleyError;
import com.atiyehandfahimeh.hw1.Constants.WeatherDataKeys;
import com.atiyehandfahimeh.hw1.Models.CityInfo;
import com.atiyehandfahimeh.hw1.Models.DayClimate;
import com.atiyehandfahimeh.hw1.Network.HandleError;
import com.atiyehandfahimeh.hw1.Network.ParseResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherParseResponse implements ParseResponse {
    private String errorMessage;
    private CityInfo cityInfo;
    @Override
    public void parseSuccessResponse(JSONObject response) {
        try {

            JSONObject location = response.getJSONObject(WeatherDataKeys.getLOCATION());
            JSONObject current = response.getJSONObject(WeatherDataKeys.getCURRENT());
            String city = location.getString(WeatherDataKeys.getNAME());
            String country = location.getString(WeatherDataKeys.getCOUNTRY());
            String lastUpdate = current.getString(WeatherDataKeys.getLastUpdated());

            JSONArray forecastDay = response.getJSONObject(WeatherDataKeys.getFORECAST())
                    .getJSONArray(WeatherDataKeys.getForecastDay());

            ArrayList<DayClimate> dayData = new ArrayList<DayClimate>();
            for(int i  = 0; i < forecastDay.length(); i++){
                JSONObject forecastCurrent = forecastDay.getJSONObject(i);

                String data = forecastCurrent.getString(WeatherDataKeys.getDATE());

                JSONObject forecastWeatherInfoTmp = forecastCurrent.getJSONObject(WeatherDataKeys.getDAY());

                Double maxTemp = forecastWeatherInfoTmp.getDouble(WeatherDataKeys.getMaxTemp());
                Double minTemp = forecastWeatherInfoTmp.getDouble(WeatherDataKeys.getMinTemp());
                Double avgTemp = forecastWeatherInfoTmp.getDouble(WeatherDataKeys.getAvgTemp());

                JSONObject forecastAirInfoTmp = forecastWeatherInfoTmp.getJSONObject(WeatherDataKeys.getCONDITION());

                String weatherText = forecastAirInfoTmp.getString(WeatherDataKeys.getTEXT());
                int photoCode = forecastAirInfoTmp.getInt(WeatherDataKeys.getCODE());
                DayClimate dayClimate = new DayClimate(data, weatherText, photoCode, maxTemp, minTemp, avgTemp);
                dayData.add(dayClimate);
            }
            cityInfo = new CityInfo(city, country, lastUpdate, dayData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseFailureResponse(VolleyError error) {
        HandleError handleError = new HandleError(error);
        Function<JSONObject, String> parseError = data->
                data.optJSONObject(WeatherDataKeys.getERROR()).optString(WeatherDataKeys.getMESSAGE());
        errorMessage = handleError.handleError(parseError);
    }

    public CityInfo getCityInfo() {
        return cityInfo;
    }


    public String getErrorMessage() {
        return errorMessage;
    }
}
