package com.atiyehandfahimeh.hw1.Place;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

import com.android.volley.VolleyError;
import com.atiyehandfahimeh.hw1.Models.Place;
import com.atiyehandfahimeh.hw1.Network.State;
import com.atiyehandfahimeh.hw1.R;
import com.atiyehandfahimeh.hw1.Weather.WeatherDisplayActivity;

import org.json.JSONObject;

import java.util.ArrayList;

public class SearchPlaceActivity extends AppCompatActivity implements PlaceAdapter.OnItemClickListener {
    private static final String EXTRA_LONGITUDE = "longitude";
    private static final String EXTRA_LATITUDE = "latitude";
    private EditText searchBox;
    private ImageView searchButton;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ArrayList<Place> placesArr;
    private PlaceAdapter placeAdapter;
    private Handler placeHandler;
    private PlaceParseResponse placeData = new PlaceParseResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);
        getViewFromXML();
        controller();
    }

    private void handleSearch(String text) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        placeHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 == State.SUCCESS.getValue()) {
                    placeData.parseSuccessResponse((JSONObject) msg.obj);
                    mainController();
                } else if (msg.arg1 == State.FAIL.getValue()) {
                    placeData.parseFailureResponse((VolleyError) msg.obj);
                    secondController();
                }

            }
        };
        Thread thread = new Thread(new PlaceAPICallerRunnable(this, placeHandler, text));
        thread.start();
//        Log.i("mainactivity", "^^^^^^^^^^^^^^^^^^^^mainactivity pid: " + android.os.Process.myPid() +
//                " Tid: " + android.os.Process.myTid() +
//                " id: " + Thread.currentThread().getId());
    }

    private void controller() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSearch(searchBox.getText().toString());
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
                            handleSearch(searchBox.getText().toString());
                            return true;
                        }
                        return false;
                    }
                }
        );
    }

    private void mainController(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        placesArr = placeData.getPlaceList();
        placeAdapter = new PlaceAdapter(this , placesArr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(placeAdapter);
        placeAdapter.setOnItemClickListener(SearchPlaceActivity.this);
        placeAdapter.notifyDataSetChanged();
    }
    private void secondController(){
        progressBar.setVisibility(View.GONE);
        Toast.makeText(
                getApplicationContext(), placeData.getErrorMessage(), Toast.LENGTH_LONG
        ).show();
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
        Place clickedItem = placesArr.get(position);

        showWeatherIntent.putExtra(EXTRA_LONGITUDE, clickedItem.getCenterX());
        showWeatherIntent.putExtra(EXTRA_LATITUDE, clickedItem.getCenterY());
        startActivity(showWeatherIntent);
    }
}
