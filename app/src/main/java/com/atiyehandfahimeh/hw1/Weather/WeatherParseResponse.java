package com.atiyehandfahimeh.hw1.Weather;

import androidx.arch.core.util.Function;

import com.android.volley.VolleyError;
import com.atiyehandfahimeh.hw1.Constants.WeatherDataKeys;
import com.atiyehandfahimeh.hw1.Models.DayWeather;
import com.atiyehandfahimeh.hw1.Network.HandleError;
import com.atiyehandfahimeh.hw1.Network.ParseResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherParseResponse implements ParseResponse {
    private String city;
    private String country;
    private String lastUpdate;
    private ArrayList<DayWeather> dayData;
    private String errorMessage;
    @Override
    public void parseSuccessResponse(JSONObject response) {
        try {

            JSONObject location = response.getJSONObject(WeatherDataKeys.getLOCATION());
            JSONObject current = response.getJSONObject(WeatherDataKeys.getCURRENT());
            city = location.getString(WeatherDataKeys.getNAME());
            country = location.getString(WeatherDataKeys.getCOUNTRY());
            lastUpdate = current.getString(WeatherDataKeys.getLastUpdated());

            JSONArray forecastDay = response.getJSONObject(WeatherDataKeys.getFORECAST())
                    .getJSONArray(WeatherDataKeys.getForecastDay());

            dayData = new ArrayList<DayWeather>();
            for(int i  = 0; i < forecastDay.length(); i++){
                DayWeather dayWeather = new DayWeather();
                JSONObject forecastCurrent = forecastDay.getJSONObject(i);

                dayWeather.setDate(forecastCurrent.getString(WeatherDataKeys.getDATE()));

                JSONObject forecastWeatherInfoTmp = forecastCurrent.getJSONObject(WeatherDataKeys.getDAY());

                dayWeather.setMaxTemp(forecastWeatherInfoTmp.getDouble(WeatherDataKeys.getMaxTemp()));
                dayWeather.setMinTemp(forecastWeatherInfoTmp.getDouble(WeatherDataKeys.getMinTemp()));
                dayWeather.setAvgTemp(forecastWeatherInfoTmp.getDouble(WeatherDataKeys.getAvgTemp()));

                JSONObject forecastAirInfoTmp = forecastWeatherInfoTmp.getJSONObject(WeatherDataKeys.getCONDITION());

                dayWeather.setWeather(forecastAirInfoTmp.getString(WeatherDataKeys.getTEXT()));
                dayWeather.setPhotoCode(forecastAirInfoTmp.getInt(WeatherDataKeys.getCODE()));
                dayData.add(dayWeather);
            }
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

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public ArrayList<DayWeather> getDayData() {
        return dayData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
