package ru.gb.android.time;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<Timer> elements;
    RecyclerView.Adapter<MyViewHolder> adapter;

    RecyclerView recyclerView;
    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;
    TimerDataSource tds;

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView timerTextView, nameTextView;

        MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_timer, parent, false));
            itemView.setOnClickListener(this);
            nameTextView = itemView.findViewById(R.id.item_name_text_view);
            timerTextView = itemView.findViewById(R.id.item_timer_text_view);
        }

        void bind(int position) {
            nameTextView.setText(elements.get(position).getName());
            timerTextView.setText(elements.get(position).getCurrentTime());
        }

        @Override
        public void onClick(View view) {
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
        //Создаем массив элементов для списка
        //elements = tds.getAllTimers();
        //Log.e("Main","init");
        tds.addTimer("MyTimer",10,15,20);
        elements = tds.getAllTimers();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.timer_list);
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, elements);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        registerForContextMenu(recyclerView);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addElement(0,1,0, "New timer");
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        // Inflate the menu; this adds items to the action bar if it is present.
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    // Метод вызывается по нажатию на любой пункт меню. В качестве агрумента приходит item меню.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_edit:
                editElement(info.position);
                return true;
            case R.id.menu_delete:
                deleteElement(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
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

    private void editElement(int id) {
        tds.editTimer(id,"Edited timer", 1,2,3);
        elements = tds.getAllTimers();
        elements.get(id).resetTimer();
        adapter.notifyDataSetChanged();
    }

    private void deleteElement(int id) {
        tds.deleteTimer(elements.get(id));
        elements = tds.getAllTimers();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
    protected void onStop() {
        tds.close();
        super.onStop();
    }
}
