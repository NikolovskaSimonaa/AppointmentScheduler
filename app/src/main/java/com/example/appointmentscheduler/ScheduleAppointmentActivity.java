package com.example.appointmentscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ScheduleAppointmentActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textView10, textView11, textView12, textView13, textView14;
    TextView textView20, textView21, textView22, textView23, textView24;
    TextView textView30, textView31, textView32, textView33, textView34;
    TextView textView40, textView41, textView42, textView43, textView44;
    TextView textView50, textView51, textView52, textView53, textView54;
    int userId;
    DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appointment);
        db = new DatabaseHandler(this);
        userId = getIntent().getIntExtra("USER_ID", -1);
        setupUILogic();
        AppointmentJobService.enqueueWork(getApplicationContext());
        disableAppointmentsAfterTheirStartTime();
    }
    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            handleTextViewClick((TextView) v);
        }
    }
    private void handleTextViewClick(TextView textView) {
        if (textView.isEnabled()) {
            textView.setEnabled(false);

            String textViewIdString = textView.getResources().getResourceEntryName(textView.getId()).substring(8);
            int lastTwoDigits = Integer.parseInt(textViewIdString);

            int appointmentId = 5 * (lastTwoDigits % 10) + lastTwoDigits /10;
            db.bookAppointmentForUser(userId, appointmentId);
            Toast.makeText(this, "Time slot booked: " + textView.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupUILogic(){

        textView10 = findViewById(R.id.textView10);  textView20 = findViewById(R.id.textView20);
        textView11 = findViewById(R.id.textView11);  textView21 = findViewById(R.id.textView21);
        textView12 = findViewById(R.id.textView12);  textView22 = findViewById(R.id.textView22);
        textView13 = findViewById(R.id.textView13);  textView23 = findViewById(R.id.textView23);
        textView14 = findViewById(R.id.textView14);  textView24 = findViewById(R.id.textView24);

        textView30 = findViewById(R.id.textView30);  textView40 = findViewById(R.id.textView40);
        textView31 = findViewById(R.id.textView31);  textView41 = findViewById(R.id.textView41);
        textView32 = findViewById(R.id.textView32);  textView42 = findViewById(R.id.textView42);
        textView33 = findViewById(R.id.textView33);  textView43 = findViewById(R.id.textView43);
        textView34 = findViewById(R.id.textView34);  textView44 = findViewById(R.id.textView44);

        textView50 = findViewById(R.id.textView50);
        textView51 = findViewById(R.id.textView51);
        textView52 = findViewById(R.id.textView52);
        textView53 = findViewById(R.id.textView53);
        textView54 = findViewById(R.id.textView54);

        //set disable on the taken appointments
        for (int i = 10; i <= 50; i += 10) {
            for(int j = 0; j <= 4; j++) {
                int textViewId = getResources().getIdentifier("textView" + (i + j), "id", getPackageName());
                TextView textView = findViewById(textViewId);

                String textViewIdString = textView.getResources().getResourceEntryName(textView.getId()).substring(8);
                int lastTwoDigits = Integer.parseInt(textViewIdString);

                int appointmentId = 5 * (lastTwoDigits % 10) + lastTwoDigits /10;
                if (db.isAppointmentTaken(appointmentId)) {
                    textView.setEnabled(false);
                }

                textView.setOnClickListener(this);
            }
        }

        textView10.setOnClickListener(this); textView20.setOnClickListener(this);
        textView11.setOnClickListener(this); textView21.setOnClickListener(this);
        textView12.setOnClickListener(this); textView22.setOnClickListener(this);
        textView13.setOnClickListener(this); textView23.setOnClickListener(this);
        textView14.setOnClickListener(this); textView24.setOnClickListener(this);

        textView30.setOnClickListener(this); textView40.setOnClickListener(this);
        textView31.setOnClickListener(this); textView41.setOnClickListener(this);
        textView32.setOnClickListener(this); textView42.setOnClickListener(this);
        textView33.setOnClickListener(this); textView43.setOnClickListener(this);
        textView34.setOnClickListener(this); textView44.setOnClickListener(this);

        textView50.setOnClickListener(this);
        textView51.setOnClickListener(this);
        textView52.setOnClickListener(this);
        textView53.setOnClickListener(this);
        textView54.setOnClickListener(this);
    }

    private void disableAppointmentsAfterTheirStartTime() {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        if (currentDay == Calendar.SATURDAY || currentDay == Calendar.SUNDAY) { // don't disable appointments on Saturday or Sunday
            return;
        }

        for (int i = 10; i <= 50; i += 10) {
            for (int j = 0; j <= 4; j++) {
                int textViewId = getResources().getIdentifier("textView" + (i + j), "id", getPackageName());
                TextView textView = findViewById(textViewId);

                String textViewIdString = textView.getResources().getResourceEntryName(textView.getId()).substring(8);
                int lastTwoDigits = Integer.parseInt(textViewIdString);

                int appointmentHour = lastTwoDigits / 10 + 9; // (+9) refers to the fact that
                                                              // calendar.get(Calendar.HOUR_OF_DAY)
                                                              // return numbers from 10-14 for a range of hours 10:00 to 14:00,
                                                              // while (lastTwoDigits / 10)
                                                              // returns numbers from 1-5 for the appropriate hours

                int appointmentDay = lastTwoDigits % 10 + 2; // (+2) refers to the fact that
                                                             // calendar.get(Calendar.DAY_OF_WEEK)
                                                             // return numbers from 2-6 for days Monday-Friday

                // disable appointments for past and current-day appointments before the current time
                if (appointmentDay < currentDay || (appointmentDay == currentDay && appointmentHour <= currentHour)) {
                    textView.setEnabled(false);
                }
            }
        }
    }
}