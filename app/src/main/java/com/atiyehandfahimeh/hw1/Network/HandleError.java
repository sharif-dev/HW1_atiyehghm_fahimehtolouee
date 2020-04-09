package com.atiyehandfahimeh.hw1.Network;

import androidx.arch.core.util.Function;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class HandleError {
    private VolleyError error;

    public HandleError(VolleyError error) {
        this.error = error;
    }

    public String handleError(Function<JSONObject, String> handleError){
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            //This indicates that the reuest has either time out or there is no connection
            return new String("There is error in your connection!");
        } else if (error instanceof AuthFailureError) {
            //Error indicating that there was an Authentication Failure while performing the request
            return new String("API key is invalid");
        } else if (error instanceof ServerError) {
            //Indicates that the server responded with a error response
            return new String("Server can't response");
        } else if (error instanceof NetworkError) {
            //Indicates that there was network error while performing the request
            return new String("Network error occurred while performing the request");
        } else if (error instanceof ParseError) {
            // Indicates that the server response could not be parsed
            return new String("Server response could not be parsed");
        }
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            return handleError.apply(data);
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
