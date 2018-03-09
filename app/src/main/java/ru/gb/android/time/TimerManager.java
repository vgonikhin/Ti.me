package ru.gb.android.time;

import android.content.Context;

import java.util.List;

public class TimerManager {

    private List<TiMeTimer> elements;
    private TimerDataSource tds;

    public TimerManager(Context context) {
        tds = new TimerDataSource(context);
        tds.open();
        elements = tds.getAllTimers();
    }

    public List<TiMeTimer> getElements() {
        return elements;
    }

    public TimerDataSource getTds() {
        return tds;
    }

    public void clearList(){
        tds.deleteAll();
        elements = tds.getAllTimers();
    }

    public void addElement(int h, int m, int s, String name) {
        tds.addTimer(name,h,m,s);
        elements = tds.getAllTimers();
    }

    public void editElement(int id, int position) {
        tds.editTimer(id,"Edited timer", 1,2,3);
        elements = tds.getAllTimers();
        elements.get(position).resetTimer();
    }

    public void deleteElement(int id) {
        tds.deleteTimer(id);
        elements = tds.getAllTimers();
    }
}
