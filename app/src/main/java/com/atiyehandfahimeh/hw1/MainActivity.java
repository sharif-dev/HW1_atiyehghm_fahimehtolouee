package com.atiyehandfahimeh.hw1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
    private Handler placehandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViewFromXML();
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
        placehandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 == 1) {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    listItems = (ArrayList<ListItem>) msg.obj;
                    listAdapter.setData(listItems);
                    listAdapter.notifyDataSetChanged();
                } else if (msg.arg1 == 2) {
                    VolleyError error = (VolleyError) msg.obj;
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);
                        String message = data.optString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        Thread placeThread = new Thread(new PlaceAPICallerRunnable(text, this, placehandler ));
        placeThread.start();
        Log.i("mainactivity", "^^^^^^^^^^^^^^^^^^^^mainactivity pid: " + android.os.Process.myPid() +
                " Tid: " + android.os.Process.myTid() +
                " id: " + Thread.currentThread().getId());
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

        showWeatherIntent.putExtra(EXTRA_LONGITUDE, clickedItem.getCenterX());
        showWeatherIntent.putExtra(EXTRA_LATITUDE, clickedItem.getCenterY());
        startActivity(showWeatherIntent);
    }
}
