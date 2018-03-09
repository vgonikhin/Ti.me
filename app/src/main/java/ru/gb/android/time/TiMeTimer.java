package ru.gb.android.time;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;
import java.util.TimerTask;

public class TiMeTimer extends TimerTask {

    private int id;
    private int startHours;
    private int startMinutes;
    private int startSeconds;
    private int hours;
    private int minutes;
    private int seconds;

    private int time;

    private boolean ticking;
    private boolean finished;

    private String name;

    private final int MAX_MINUTES = 60;
    private final int MAX_SECONDS = 60;

    public TiMeTimer(int id, int hours, int minutes, int seconds, String name, int ticking) {
        this.id = id;
        this.startHours = hours;
        this.startMinutes = minutes;
        this.startSeconds = seconds;
        this.time = hours*MAX_MINUTES*MAX_SECONDS + minutes*MAX_SECONDS + seconds;
        this.name = name;
        this.ticking = (ticking==1);
        this.finished = false;
        resetTimer();
    }

    public boolean isTicking() {
        return ticking;
    }

    public void setTicking(boolean ticking) {
        this.ticking = ticking;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getCurrentTime(){
        return String.format(Locale.ENGLISH,"%d:%02d:%02d",this.getHours(), this.getMinutes(), this.getSeconds());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private int getHours() {
        return hours;
    }

    private int getMinutes() {
        return minutes;
    }

    private int getSeconds() {
        return seconds;
    }

    public void resetTimer(){
        hours = startHours;
        minutes = startMinutes;
        seconds = startSeconds;
        time = hours*MAX_MINUTES*MAX_SECONDS + minutes*MAX_SECONDS + seconds;
        ticking = false;
        finished = false;
    }

    public String startTimer(){
        setTicking(true);
        return getName() + " started";
    }

    public String pauseTimer(){
        setTicking(false);
        return getName() + " paused";
    }

    public void showMessage(Context context, String message){
        Toast.makeText(context,getName() + " finished working", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void run() {
        String TAG = "TiMeTimer";
        Log.e(TAG, getId() + " " + getName() + " run " + isTicking() + " time: " + time);
        if(isTicking()){
            time--;
            hours = time/(MAX_MINUTES*MAX_SECONDS);
            minutes = (time%(MAX_SECONDS*MAX_MINUTES))/MAX_SECONDS;
            seconds = time%MAX_SECONDS;
            if(time==0){
                resetTimer();
            }
        }
    }
}
