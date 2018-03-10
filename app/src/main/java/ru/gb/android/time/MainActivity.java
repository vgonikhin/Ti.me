package ru.gb.android.time;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Support elements
    public static TimerManager tm;

    //Common GUI elements
    private RecyclerView.Adapter<TimerItemViewHolder> adapter;
    private DrawerLayout drawer;

    //RecyclerView Item Holder
    private class TimerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //ViewHolder GUI elements
        private TextView timerTextView, nameTextView;
        private ImageButton startImageButton, pauseImageButton, editImageButton, deleteImageButton;

        //Constructor
        TimerItemViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_timer, parent, false));
            initializeGUI();
        }

        //Initialize GUI elements
        private void initializeGUI() {
            itemView.setOnClickListener(this);
            nameTextView = itemView.findViewById(R.id.item_name_text_view);
            timerTextView = itemView.findViewById(R.id.item_timer_text_view);
            startImageButton = itemView.findViewById(R.id.item_start_image_button);
            pauseImageButton = itemView.findViewById(R.id.item_pause_image_button);
            editImageButton = itemView.findViewById(R.id.item_edit_image_button);
            deleteImageButton = itemView.findViewById(R.id.item_delete_image_button);
            startImageButton.setOnClickListener(this);
            pauseImageButton.setOnClickListener(this);
            editImageButton.setOnClickListener(this);
            deleteImageButton.setOnClickListener(this);
        }

        void bind(int position) {
            nameTextView.setText(tm.getElements().get(position).getName()); //Display timer name
            timerTextView.setText(tm.getElements().get(position).getCurrentTime()); //Display timer time
            updateButtonVisibility(); //Check timer state and show Start/Pause button
        }

        //Process button events
        @Override
        public void onClick(View view) {
            TiMeTimer timer = tm.getElements().get(this.getAdapterPosition()); //Determine the timer we're working with
            switch (view.getId()) {
                case R.id.item_start_image_button: //Start selected timer
                    if(tm.getActiveElementsNo()==0)
                        onStartService();
                    tm.startTimer(this.getAdapterPosition());
                    updateButtonVisibility(); //Show Pause button
                    return;
                case R.id.item_pause_image_button: //Pause selected timer
                    tm.pauseTimer(this.getAdapterPosition());
                    updateButtonVisibility(); //Show Start button
                    if(tm.getActiveElementsNo()==0)
                        onStopService();
                    return;
                case R.id.item_edit_image_button: //Edit selected timer
                    changeElement(false, timer.getName(), this.getAdapterPosition(), timer.getStartHours(), timer.getStartMinutes(), timer.getStartSeconds());
                    if(tm.getActiveElementsNo()==0)
                        onStopService();
                    return;
                case R.id.item_delete_image_button: //Delete selected timer
                    deleteElements(this.getAdapterPosition());
                    if(tm.getActiveElementsNo()==0)
                        onStopService();
            }
        }

        //Display Start/Pause button according to timer state
        private void updateButtonVisibility() {
            if (!tm.getElements().get(this.getAdapterPosition()).isTicking()) { //If active, then show Pause button
                startImageButton.setVisibility(View.VISIBLE);
                pauseImageButton.setVisibility(View.INVISIBLE);
            } else { //If inactive, then show Start button
                startImageButton.setVisibility(View.INVISIBLE);
                pauseImageButton.setVisibility(View.VISIBLE);
            }
        }

    }

    //RecyclerView Adapter
    private class TimerListAdapter extends RecyclerView.Adapter<TimerItemViewHolder> {

        @Override
        public TimerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this.getApplicationContext());
            return new TimerItemViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(TimerItemViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return tm.getElements().size();
        }
    }

    //Activity default methods

    //Activity lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //Choosing layout

        tm = new TimerManager(this); //Initializing support elements

        initializeGUI(); //Initializing GUI

        if(tm.getActiveElementsNo()!=0)
            onStartService(); //Starting service

        //Setting up update every second
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged(); //Update timer list
                //Toast.makeText(MainActivity.this,"1 sec",Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 1000);
            }
        });
    }

    //Initializing GUI
    private void initializeGUI(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rvTimerList = findViewById(R.id.timer_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(VERTICAL);
        rvTimerList.setLayoutManager(layoutManager);
        adapter = new TimerListAdapter();
        rvTimerList.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeElement(true, getString(R.string.name_new_timer));
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onDestroy() {
        tm.getTds().close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!tm.getTds().isOpen()) {
            tm.getTds().open();
        }
        tm.reinitialize();
    }

    //GUI element callbacks
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
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else if (id == R.id.nav_profile) { //Go to profile
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        } else if (id == R.id.nav_settings) { //Go to settings
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear: //Clear timer list
                deleteElements(-1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Methods for working with services
    public void onStartService() {
        Intent intent = new Intent(getBaseContext(), StartedService.class);
        intent.putExtra(StartedService.SERVICE_PARAMETER, 1);
        startService(intent);
    }

    public void onStopService() {
        Intent intent = new Intent(getBaseContext(), StartedService.class);
        stopService(intent);
    }

    //Methods for handling Timer list actions
    //Adding or editing a timer
    private void changeElement(boolean isNew, String name, final int... args) {

        AlertDialog.Builder adb = new AlertDialog.Builder(this); //New Alert dialog builder
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.dialog_add_edit, null); //Choosing layout for the dialog

        //Initializing GUI elements
        //Title
        final TextView titleTextView = v.findViewById(R.id.title_text_view);
        //Number pickers
        final NumberPicker npHours = v.findViewById(R.id.npHours);
        npHours.setMinValue(0);
        npHours.setMaxValue(23);
        final NumberPicker npMinutes = v.findViewById(R.id.npMinutes);
        npMinutes.setMinValue(0);
        npMinutes.setMaxValue(59);
        final NumberPicker npSeconds = v.findViewById(R.id.npSeconds);
        npSeconds.setMinValue(0);
        npSeconds.setMaxValue(59);
        //Setting values for display
        if (!isNew) { //For editing an existing timer
            titleTextView.setText(R.string.addedit_dialog_title_edit);
            npHours.setValue(args[1]);
            npMinutes.setValue(args[2]);
            npSeconds.setValue(args[3]);
        } else { //For adding a new timer
            titleTextView.setText(R.string.addedit_dialog_title_add);
            npHours.setValue(0);
            npMinutes.setValue(5);
            npSeconds.setValue(0);
        }
        //Setting format for minutes and seconds
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format(Locale.ENGLISH, "%02d", i);
            }
        };
        npMinutes.setFormatter(formatter);
        npSeconds.setFormatter(formatter);
        //Timer name
        final EditText editText = v.findViewById(R.id.timer_name_edit_text);
        editText.setText(name);

        //Defining buttons
        if (isNew) { //For editing an existing timer
            adb.setPositiveButton(R.string.button_ok_addtimer, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    tm.addElement(npHours.getValue(), npMinutes.getValue(), npSeconds.getValue(), editText.getText().toString());
                }
            }).setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MainActivity.this, R.string.message_cancel_add, Toast.LENGTH_SHORT).show();
                }
            });
        } else { //For adding a new timer
            adb.setPositiveButton(R.string.button_ok_edittimer, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    tm.editElement(args[0], npHours.getValue(), npMinutes.getValue(), npSeconds.getValue(), editText.getText().toString());
                }
            }).setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MainActivity.this, R.string.message_cancel_edit, Toast.LENGTH_SHORT).show();
                }
            });
        }

        //All defined, showing the dialog
        adb.setView(v);
        AlertDialog ad = adb.create();
        ad.show();

        //Updating Timer list
        adapter.notifyDataSetChanged();
    }

    //Deleting timer(s)
    private void deleteElements(final int position) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this); //New Alert dialog builder

        //Initializing GUI elements
        //Label
        final TextView questionTextView = new TextView(this);
        questionTextView.setGravity(Gravity.CENTER);
        questionTextView.setMinHeight(200);
        questionTextView.setAllCaps(true);
        questionTextView.setTextSize(18);
        questionTextView.setTextColor(Color.parseColor(getString(R.string.dialog_text_color)));
        questionTextView.setText(R.string.delete_dialog_label);

        //Defining buttons
        if (position==-1) { //For clearing the list
            adb.setPositiveButton(R.string.button_ok_clear, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    tm.clearList();
                }
            }).setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MainActivity.this, R.string.message_cancel_clear, Toast.LENGTH_SHORT).show();
                }
            });
        } else { //For deleting the selected timer
            adb.setPositiveButton(R.string.button_ok_deletetimer, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    tm.deleteElement(position);
                }
            }).setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MainActivity.this, R.string.message_cancel_delete, Toast.LENGTH_SHORT).show();
                }
            });
        }

        //All defined, showing the dialog
        adb.setView(questionTextView);
        AlertDialog ad = adb.create();
        ad.show();

        //Updating Timer list
        adapter.notifyDataSetChanged();
    }

}
