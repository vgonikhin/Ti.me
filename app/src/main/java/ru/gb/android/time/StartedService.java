package ru.gb.android.time;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;

public class StartedService extends IntentService {

    //Constants
    public static final String SERVICE_PARAMETER = "ServiceParameter";
    private static final String TAG = "StartedService";

    //Support elements
    Timer timer;

    //Constructor
    public StartedService() {
        super(TAG);
        timer = new Timer();
    }

    //Service default methods
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras(); //Getting input
        int n = 0; //Number of active timers
        if (bundle != null) {
            n = bundle.getInt(SERVICE_PARAMETER,1); //Getting values from input
        }
        Toast.makeText(this,n+" active timers",Toast.LENGTH_SHORT).show();
        if(n != 0){ //If at least one active timer
            for(TiMeTimer t : MainActivity.tm.getElements()){ //Schedule timer tasks
                timer.purge();
                timer.schedule(t,1000,1000);
                Toast.makeText(this,t.getName()+" scheduled",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { //Only logging for now
        Log.w(TAG, "onStartCommand");
        //Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() { //Only logging for now
        Log.w(TAG, "onDestroy");
        //Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}