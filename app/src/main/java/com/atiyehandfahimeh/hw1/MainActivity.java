package com.atiyehandfahimeh.hw1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private EditText searchBox;
    private RecyclerView recyclerView;

    private ListAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViewFromXML();
        controller();
        APICall("tehran");
    }

    private void APICall(String text) {
        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + text +
                ".json?" +
                "access_token=pk.eyJ1IjoiZmFoaW0tdCIsImEiOiJjazhtY3dwc3AwMHJzM2ducW9hcGF3N2cwIn0.2BsWu6j2i863YTGzlFikcQ";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<ListItem> listItems = new ArrayList<>();
                        try{
                            for (int i = 0; i < response.optJSONArray("features").length(); i++) {
                                ListItem listItem = new ListItem();
                                listItem.setId(response.optJSONArray("features").optJSONObject(i).optString("id"));
                                listItem.setName(response.optJSONArray("features").optJSONObject(i).optString("place_name"));
                                listItem.setCenterX(response.optJSONArray("features").optJSONObject(i).optJSONArray("center").getLong(0));
                                listItem.setCenterY(response.optJSONArray("features").optJSONObject(i).optJSONArray("center").getLong(1));
                                listItems.add(listItem);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        listAdapter.setData(listItems);
                        listAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: 4/5/20
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void controller() {
        listAdapter = new ListAdapter(this , new ArrayList<ListItem>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    private void getViewFromXML() {
        searchBox = findViewById(R.id.searchBox);
        recyclerView = findViewById(R.id.listItem);
    }

    //called when user selects a city
    public void sendMessage(View view) {
        Intent intent = new Intent(this, WeatherDisplayActivity.class);
        //with putExtra i get x and y
        //intent.putExtra(x, y, message);
        startActivity(intent);
    }
}
