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
        if(elements.size()==0){
            elements.add(new TiMeTimer(1,h,m,s,name,0));
        } else {
            elements.add(new TiMeTimer(tds.getNextId(), h, m, s, name, 0));
        }
        tds.addTimer(name,h,m,s);
        //elements = tds.getAllTimers();
    }

    public void editElement(int position, int h, int m, int s, String name) {
        elements.get(position).editTimer(name,h,m,s);
        tds.editTimer(elements.get(position).getId(),name, h,m,s);
        //elements = tds.getAllTimers();
        //elements.get(position).resetTimer();
    }

    public void deleteElement(int position) {
        elements.remove(position);
        tds.deleteTimer(elements.get(position).getId());
        //elements = tds.getAllTimers();
    }

    public int activeElementsNo(){
        int number = 0;
        for (TiMeTimer t : elements){
            if (t.isTicking())
                number++;
        }
        return number;
    }
}
