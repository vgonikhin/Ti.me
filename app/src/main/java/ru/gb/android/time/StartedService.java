package ru.gb.android.time;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;

public class StartedService extends IntentService {
    private static final String TAG = "StartedService";
    Timer timer;

    public StartedService() {
        super("StartedService");
        timer = new Timer();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        int n = 0;
        if (bundle != null) {
            n = bundle.getInt("ServiceParameter",1);
        }

        if(n != 0){
            for(TiMeTimer t : MainActivity.tm.getElements()){
                timer.purge();
                timer.schedule(t,1000,1000);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartCommand");
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        Log.w(TAG, "onDestroy");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}

