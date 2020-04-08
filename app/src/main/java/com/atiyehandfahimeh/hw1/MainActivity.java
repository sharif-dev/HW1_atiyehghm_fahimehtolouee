package com.atiyehandfahimeh.hw1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListAdapter.OnItemClickListener {
    private static final String EXTRA_LONGITUDE = "longitude";
    private static final String EXTRA_LATITUDE = "latitude";
    private EditText searchBox;
    private ImageView searchButton;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ArrayList<ListItem> listItems;
    private ListAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViewFromXML();
        Log.i("Main : ", "id: " + Thread.currentThread().getId());
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APICall(searchBox.getText().toString());
            }
        });
        searchBox.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH
                                || actionId == EditorInfo.IME_ACTION_DONE
                                || event.getAction() == KeyEvent.ACTION_DOWN
                                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            APICall(searchBox.getText().toString());
                            return true;
                        }
                        return false;
                    }
                }
        );
        controller();
    }

    private void APICall(String text) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + text +
                ".json?" +
                "access_token=pk.eyJ1IjoiZmFoaW0tdCIsImEiOiJjazhtY3dwc3AwMHJzM2ducW9hcGF3N2cwIn0.2BsWu6j2i863YTGzlFikcQ";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listItems = new ArrayList<>();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Log.i("Vooley: ", "id: " + Thread.currentThread().getId());
                        try{
                            JSONArray places = response.optJSONArray(DataKeys.getFEATURES());
                            for (int i = 0; i < places.length(); i++) {
                                JSONObject currentPlace = places.optJSONObject(i);
                                String id = currentPlace.optString(DataKeys.getID());
                                String name = currentPlace.optString(DataKeys.getPlaceName());
                                double centerX = currentPlace.optJSONArray(DataKeys.getCENTER()).getDouble(0);
                                double centerY = currentPlace.optJSONArray(DataKeys.getCENTER()).getDouble(1);
                                listItems.add(new ListItem(id, name, centerX, centerY));
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
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    String message = data.optString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void controller() {
        listAdapter = new ListAdapter(this , new ArrayList<ListItem>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(MainActivity.this);
    }

    private void getViewFromXML() {
        searchBox = findViewById(R.id.searchBox);
        searchButton = findViewById(R.id.searchButton);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.listItem);
    }

    @Override
    public void onItemClick(int position) {
        Intent showWeatherIntent = new Intent(this, WeatherDisplayActivity.class);
        ListItem clickedItem = listItems.get(position);

        showWeatherIntent.putExtra(EXTRA_LONGITUDE, Double.toString(clickedItem.getCenterX()));
        showWeatherIntent.putExtra(EXTRA_LATITUDE, Double.toString(clickedItem.getCenterY()));
        startActivity(showWeatherIntent);
    }
}
