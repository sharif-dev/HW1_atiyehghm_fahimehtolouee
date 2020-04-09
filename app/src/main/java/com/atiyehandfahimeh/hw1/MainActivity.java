package com.atiyehandfahimeh.hw1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.atiyehandfahimeh.hw1.Network.InternetConnection;
import com.atiyehandfahimeh.hw1.Place.SearchPlaceActivity;
import com.atiyehandfahimeh.hw1.Weather.WeatherDisplayActivity;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);
//        final Class<? extends Activity> activityClass;
//        InternetConnection connection = new InternetConnection(this);
//        if(connection.isConnected()){
//            Log.i("__Activity__", "First");
//            activityClass = SearchPlaceActivity.class;
//        } else{
//            Log.i("__Activity__", "Second");
//            activityClass = WeatherDisplayActivity.class;
//        }
//        Intent newActivity = new Intent(getApplicationContext(), activityClass);
//        startActivity(newActivity);

        Handler checkNetworkHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Intent newActivity = new Intent(getApplicationContext(), (Class<? extends Activity>) msg.obj);
                startActivity(newActivity);
            }
        };
        Thread thread = new Thread(new CheckNetworkRunnable(checkNetworkHandler, this));
        thread.start();
    }

}

class CheckNetworkRunnable implements Runnable{
    private Handler handler;
    private Context context;
    public CheckNetworkRunnable(Handler handler, Context context) {
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void run() {
        Message mUi = Message.obtain();
        InternetConnection connection = new InternetConnection(context);
        Class<? extends Activity> firstClass = SearchPlaceActivity.class;
        Class<? extends Activity> secondClass = WeatherDisplayActivity.class;
        if (connection.isConnected()){
            mUi.obj = firstClass;
        }
        else {
            mUi.obj = secondClass;
        }
        handler.sendMessage(mUi);
    }
}
