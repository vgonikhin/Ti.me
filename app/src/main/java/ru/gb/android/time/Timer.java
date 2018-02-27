package ru.gb.android.time;

import android.util.Log;

public class Timer {

    private int id;

    private int startHours;
    private int startMinutes;
    private int startSeconds;

    private int hours;
    private int minutes;
    private int seconds;

    private final int MAX_MINUTES = 60;
    private final int MAX_SECONDS = 60;

    public Timer(int hours, int minutes, int seconds) {
        this.startHours = hours;
        this.startMinutes = minutes;
        this.startSeconds = seconds;
        resetTimer();
        //Log.e("Timer", "constructor");
    }

    public String getCurrentTime(){
        return String.format("%2d:%2d:%2d",this.getHours(), this.getMinutes(), this.getSeconds());
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void resetTimer(){
        hours = startHours;
        minutes = startMinutes;
        seconds = startSeconds;
    }
}
