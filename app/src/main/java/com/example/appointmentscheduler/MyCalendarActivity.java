package com.example.appointmentscheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MyCalendarActivity extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    private RecyclerView recyclerView;
    private AppointmentAdapter appointmentAdapter;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar);

        databaseHandler = new DatabaseHandler(this);

        userId = getIntent().getIntExtra("USER_ID", -1);

        recyclerView = findViewById(R.id.recyclerViewAppointments);
        List<AppointmentModel> appointments=databaseHandler.getAppointmentsForUser(userId);
        appointmentAdapter = new AppointmentAdapter(this, appointments, databaseHandler, userId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(appointmentAdapter);
    }
}