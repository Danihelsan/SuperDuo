package it.jaschke.alexandria.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Danihelsan
 */
public class ConnectivityHelper {
    private enum ConnectionStatus{
        NOT_CONNECTED, WIFI, MOBILE
    }

    private ConnectionStatus connectionStatus;
    private final IntentFilter intentFilter = new IntentFilter();
    private final BroadcastReceiver connectionReceiver;

    private static ConnectivityHelper helper;

    public ConnectivityHelper() {
        this.connectionStatus = ConnectionStatus.NOT_CONNECTED;
        this.intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        this.connectionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getConnectionStatus(context);
            }
        };
    }

    public static ConnectivityHelper get(){
        if (helper==null){
            helper = new ConnectivityHelper();
        }
        return helper;
    }

    public void registerReceiver(Context context){
        getConnectionStatus(context);
        if (connectionReceiver!=null){
            context.registerReceiver(connectionReceiver, intentFilter, null, null);
        }
    }


    public void unregisterReceiver(Context context){
        if (connectionReceiver!=null){
            context.unregisterReceiver(connectionReceiver);
        }
        getConnectionStatus(context);
    }

    public ConnectionStatus getConnectionStatus(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (activeNetwork != null) {

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                    return ConnectionStatus.WIFI;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                    return ConnectionStatus.MOBILE;
            }
        return ConnectionStatus.NOT_CONNECTED;
    }

    public boolean isConnected(){
        return connectionStatus ==ConnectionStatus.WIFI || connectionStatus == ConnectionStatus.MOBILE;
    }
}
