package ru.gb.android.time;

import android.content.Context;

import java.util.List;

class TimerManager { //Class for managing timer pool

    //Support elements
    private List<TiMeTimer> elements; //Timer list
    private TimerDataSource tds; //Database interface

    //Constructor
    TimerManager(Context context) {
        tds = new TimerDataSource(context);
        tds.open();
        elements = tds.getAllTimers();
    }

    //Reinitialize
    void reinitialize(){
        elements = tds.getAllTimers();
    }

    //Getters
    List<TiMeTimer> getElements() {
        return elements;
    }

    TimerDataSource getTds() {
        return tds;
    }

    int getActiveElementsNo(){
        int number = 0;
        for (TiMeTimer t : elements){
            if (t.isTicking())
                number++;
        }
        return number; //Number of running timers
    }

    //Methods for working with the timer list
    //Adding a timer
    void addElement(int h, int m, int s, String name) {
        if(elements.size()==0){ //If the list is empty
            tds.addTimer(name,h,m,s); //First adding to the database
            elements = tds.getAllTimers(); //Then reading from the database to the list
        } else { //If the list is not empty
            elements.add(new TiMeTimer(tds.getNextId(), h, m, s, name, 0)); //First adding to the list with the next ID from the database
            tds.addTimer(name,h,m,s); //Then adding the timer to the database
        }
    }

    //Editing a timer
    void editElement(int position, int h, int m, int s, String name) {
        elements.get(position).editTimer(name,h,m,s); //Editing the list item
        tds.editTimer(elements.get(position)); //Updating the timer in the database
    }

    //Deleting a timer
    void deleteElement(int position) {
        elements.remove(position); //Removing the list item
        tds.deleteTimer(elements.get(position)); //Deleting the row from the database
    }

    //Deleting all timers
    void clearList(){
        elements.clear(); //Clearing the list
        tds.deleteAll(); //Deleting all rows from the database
    }

    //Starting a timer
    void startTimer(int position){
        elements.get(position).startTimer(); //Activating the list item
        tds.editTimer(elements.get(position).getId(),1); //Updating the column 'ticking' in the database
    }

    //Pausing a timer
    void pauseTimer(int position){
        elements.get(position).pauseTimer(); //Deactivating the list item
        tds.editTimer(elements.get(position).getId(),0); //Updating the column 'ticking' in the database
    }
}