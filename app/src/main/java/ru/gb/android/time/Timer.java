package ru.gb.android.time;

public class Timer {

    private int id;

    private int startHours;
    private int startMinutes;
    private int startSeconds;

    private int hours;
    private int minutes;
    private int seconds;

    private String name;

    private final int MAX_MINUTES = 60;
    private final int MAX_SECONDS = 60;

    public Timer(int id, int hours, int minutes, int seconds, String name) {
        this.id = id;
        this.startHours = hours;
        this.startMinutes = minutes;
        this.startSeconds = seconds;
        this.name = name;
        resetTimer();
    }

    public String getCurrentTime(){
        return String.format("%2d:%2d:%2d",this.getHours(), this.getMinutes(), this.getSeconds());
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
}
