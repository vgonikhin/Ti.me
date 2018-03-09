package ru.gb.android.time;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<TiMeTimer> elements;
    RecyclerView.Adapter<MyViewHolder> adapter;
    Timer timer;

    RecyclerView recyclerView;
    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;
    TimerDataSource tds;

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int id;
        private TextView timerTextView, nameTextView;
        private ImageButton startImageButton, pauseImageButton, editImageButton, deleteImageButton;

        MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_timer, parent, false));
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
            nameTextView.setText(elements.get(position).getName());
            timerTextView.setText(elements.get(position).getCurrentTime());
            setStartPauseVisibility();
            this.id = elements.get(position).getId();
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.category_item:
                case R.id.item_name_text_view:
                case R.id.item_timer_text_view:
                    Toast.makeText(MainActivity.this.getApplicationContext(), "Clicked",Toast.LENGTH_SHORT).show();
                    return;
                case R.id.item_start_image_button:
                    Toast.makeText(MainActivity.this.getApplicationContext(), elements.get(this.getAdapterPosition()).startTimer(),Toast.LENGTH_SHORT).show();
                    tds.editTimer(this.id,1);
                    setStartPauseVisibility();
                    return;
                case R.id.item_pause_image_button:
                    Toast.makeText(MainActivity.this.getApplicationContext(), elements.get(this.getAdapterPosition()).pauseTimer(),Toast.LENGTH_SHORT).show();
                    tds.editTimer(this.id,0);
                    setStartPauseVisibility();
                    return;
                case R.id.item_edit_image_button:
                    editElement(this.id, this.getAdapterPosition());
                    return;
                case R.id.item_delete_image_button:
                    deleteElement(this.id);
            }

        }

        private void setStartPauseVisibility() {
            if(!elements.get(this.getAdapterPosition()).isTicking()){
                startImageButton.setVisibility(View.VISIBLE);
                pauseImageButton.setVisibility(View.INVISIBLE);
            } else {
                startImageButton.setVisibility(View.INVISIBLE);
                pauseImageButton.setVisibility(View.VISIBLE);
            }
        }

    }

    //Адаптер для RecyclerView
    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this.getApplicationContext());
            return new MyViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return elements.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tds = new TimerDataSource(this);
        tds.open();
        elements = tds.getAllTimers();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.timer_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTimer();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        timer = new Timer();
        for(TiMeTimer t : elements){
            timer.schedule(t,1000,1000);
        }

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                for(TiMeTimer t : elements){
                    if(t.isFinished()) {
                        Toast.makeText(MainActivity.this, t.getName() + " finished working", Toast.LENGTH_SHORT).show();
                        t.setFinished(false);
                    }
                }
                handler.postDelayed(this,1000);
            }
        });
    }

    private void addTimer() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.dialog_add_edit,null);

        final NumberPicker npHours = v.findViewById(R.id.npHours);
        final NumberPicker npMinutes = v.findViewById(R.id.npMinutes);
        final NumberPicker npSeconds = v.findViewById(R.id.npSeconds);
        npHours.setMinValue(0);
        npHours.setMaxValue(23);
        npMinutes.setMinValue(0);
        npMinutes.setMaxValue(59);
        npSeconds.setMinValue(0);
        npSeconds.setMaxValue(59);
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d",i);
            }
        };
        npMinutes.setFormatter(formatter);
        npSeconds.setFormatter(formatter);

        final EditText editText = v.findViewById(R.id.timer_name_edit_text);

        adb.setPositiveButton("Add timer", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addElement(npHours.getValue(),npMinutes.getValue(),npSeconds.getValue(), editText.getText().toString());
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this,"Nothing added",Toast.LENGTH_SHORT).show();
            }
        });
        adb.setView(v);
        AlertDialog ad = adb.create();
        ad.show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                clearList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearList() {
        tds.deleteAll();
        elements = tds.getAllTimers();
        adapter.notifyDataSetChanged();
    }

    private void addElement(int h, int m, int s, String name) {
        tds.addTimer(name,h,m,s);
        elements = tds.getAllTimers();
        adapter.notifyDataSetChanged();
    }

    private void editElement(int id, int position) {
        tds.editTimer(id,"Edited timer", 1,2,3);
        elements = tds.getAllTimers();
        elements.get(position).resetTimer();
        adapter.notifyDataSetChanged();
    }

    private void deleteElement(int id) {
        tds.deleteTimer(id);
        elements = tds.getAllTimers();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_timers) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        tds.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!tds.isOpen()) {
            tds.open();
        }
    }
}
