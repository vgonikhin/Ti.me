package ru.gb.android.time;

import android.util.Log;

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
        resetTimer();
    }

    public boolean isTicking() {
        return ticking;
    }

    public void setTicking(boolean ticking) {
        this.ticking = ticking;
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
    }

    public String startTimer(){
        setTicking(true);
        return getName() + " started";
    }

    public String pauseTimer(){
        setTicking(false);
        return getName() + " paused";
    }

    @Override
    public void run() {
        //Log.e("Timer", getId() + " run " + isTicking());
        if(isTicking()){
            time--;
            hours = time/(MAX_MINUTES*MAX_SECONDS);
            minutes = (time%(MAX_SECONDS*MAX_MINUTES))/MAX_SECONDS;
            seconds = time%MAX_SECONDS;
        }
    }
}
