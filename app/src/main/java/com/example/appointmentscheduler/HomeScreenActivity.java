package com.example.appointmentscheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {
    DatabaseHandler db;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        db = new DatabaseHandler(this);

        userId = getIntent().getIntExtra("USER_ID", -1);

        appBarLayout = findViewById(R.id.appBarLayout2);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_my_appointments) {
            Intent intent = new Intent(HomeScreenActivity.this, MyCalendarActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        } else if (id == R.id.action_profile) {
            Intent intent = new Intent(HomeScreenActivity.this, ProfileActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void makeAppointmentButtonClicked(View view){
        Intent intent = new Intent(HomeScreenActivity.this, ScheduleAppointmentActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }
}