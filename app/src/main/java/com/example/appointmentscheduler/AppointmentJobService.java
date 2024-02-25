package com.example.appointmentscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.util.Calendar;

public class AppointmentJobService extends JobIntentService {
    private static final String TAG = "AppointmentService";

    private static final int JOB_ID = 1000;
    private DatabaseHandler db;

    public AppointmentJobService() {
        super();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        db = new DatabaseHandler(getApplicationContext());
        // update appointments every weekend
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            db.updateAppointments();
        }
        db.close();
    }
    public static void enqueueWork(Context context) {
        enqueueWork(context, AppointmentJobService.class, JOB_ID, new Intent());
    }
}