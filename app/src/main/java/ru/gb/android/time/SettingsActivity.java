package ru.gb.android.time;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{ //Activity for managing settings and preferences

    //Constants
    public static final String NOTIFICATIONS_ENABLED = "notifications_enabled";

    //Support elements
    private SharedPreferences sPref;

    //Common GUI elements
    private DrawerLayout drawer;
    private SwitchCompat switchNotifications;

    //Activity default methods

    //Activity lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); //Choosing layout

        sPref = getSharedPreferences("SharedPrefs", MODE_PRIVATE); //Initializing support elements

        initializeGUI(); //Initializing GUI elements

        loadPrefs(); //Loading shared preferences
    }

    //Initializing GUI elements
    private void initializeGUI(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        switchNotifications = findViewById(R.id.set_show_notifications);
    }

    @Override
    protected void onPause(){
        savePrefs(); //Saving shared preferences
        super.onPause();
    }

    @Override
    protected void onResume() {
        loadPrefs(); //Loading shared preferences
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_timers) { //Go to timers (Main Activity)
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        } else if (id == R.id.nav_profile) { //Go to profile
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        } else if (id == R.id.nav_settings) { //Go to settings
            startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Methods
    //Saving shared preferences
    private void savePrefs(){
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(NOTIFICATIONS_ENABLED, switchNotifications.isChecked());
        ed.apply();
    }

    //Loading shared preferences
    private void loadPrefs(){
        boolean notif = sPref.getBoolean(NOTIFICATIONS_ENABLED,false);
        switchNotifications.setChecked(notif);
    }

}
