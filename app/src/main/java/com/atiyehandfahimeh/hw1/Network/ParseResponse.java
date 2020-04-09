package com.atiyehandfahimeh.hw1.Network;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface ParseResponse {

    public void parseSuccessResponse(JSONObject response);
    public void parseFailureResponse(VolleyError error);
}
