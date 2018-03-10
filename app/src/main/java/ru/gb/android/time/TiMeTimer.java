package ru.gb.android.time;

import android.util.Log;
import android.widget.Toast;

import java.util.Locale;
import java.util.TimerTask;

public class TiMeTimer extends TimerTask {

    //Constants
    private final int MAX_MINUTES = 60;
    private final int MAX_SECONDS = 60;

    //Fields
    private int id;
    private boolean ticking;
    private String name;
    private int time;
    private int startHours, startMinutes, startSeconds;
    private int hours, minutes, seconds;

    //Constructor
    TiMeTimer(int id, int hours, int minutes, int seconds, String name, int ticking) {
        this.id = id;
        this.startHours = hours;
        this.startMinutes = minutes;
        this.startSeconds = seconds;
        this.time = hours*MAX_MINUTES*MAX_SECONDS + minutes*MAX_SECONDS + seconds;
        this.name = name;
        this.ticking = (ticking==1);
        resetTimer();
    }

    //Getters Setters
    public int getId() {
        return id;
    }

    boolean isTicking() {
        return ticking;
    }

    private void setTicking(boolean ticking) {
        this.ticking = ticking;
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

    int getStartHours() {
        return startHours;
    }

    int getStartMinutes() {
        return startMinutes;
    }

    int getStartSeconds() {
        return startSeconds;
    }

    //Methods
    //Current time as a String for display
    String getCurrentTime(){
        return String.format(Locale.ENGLISH,"%d:%02d:%02d",this.getHours(), this.getMinutes(), this.getSeconds());
    }

    //Change timer values and reset
    void editTimer(String name, int h, int m, int s){
        this.startHours = h;
        this.startMinutes = m;
        this.startSeconds = s;
        this.time = h*MAX_MINUTES*MAX_SECONDS + m*MAX_SECONDS + s;
        this.name = name;
        this.ticking = false;
        resetTimer();
    }

    //Stop timer and revert to starting values
    private void resetTimer(){
        hours = startHours;
        minutes = startMinutes;
        seconds = startSeconds;
        time = hours*MAX_MINUTES*MAX_SECONDS + minutes*MAX_SECONDS + seconds;
        ticking = false;
    }

    //Activate timer
    void startTimer(){
        setTicking(true);
    }

    //Deactivate timer
    void pauseTimer(){
        setTicking(false);
    }

    //Process one second
    @Override
    public void run() {
        Log.d("TiMeTimer", getId() + " " + getName() + " run " + isTicking() + " time: " + time);
        if(isTicking()){ //If the timer is active
            time--; //Update time, hours, minutes, seconds
            hours = time/(MAX_MINUTES*MAX_SECONDS);
            minutes = (time%(MAX_SECONDS*MAX_MINUTES))/MAX_SECONDS;
            seconds = time%MAX_SECONDS;
            if(time==0){ //If the timer ran out
                resetTimer();
            }
        }
    }
}