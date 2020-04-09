package com.atiyehandfahimeh.hw1.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class InternetConnection {
    private boolean isConnected;

    public InternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
            this.isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
//        Log.i("__Connection__", activeNetwork.toString());
//        Log.i("__Connection__", (Boolean.toString(activeNetwork.isConnectedOrConnecting())));



    }

    public boolean isConnected() {
        return isConnected;
    }
}
