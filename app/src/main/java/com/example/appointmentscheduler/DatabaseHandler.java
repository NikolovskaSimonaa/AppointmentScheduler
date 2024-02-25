package com.example.appointmentscheduler;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import com.example.appointmentscheduler.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="AppointmentScheduler.db";
    private static final int DATABASE_VERSION = 6;

    public static final String COLUMN_ID = "_id";

    private static final String TABLE_USER="user";
    private static final String COLUMN_NAME="name";
    private static final String COLUMN_SURNAME="surname";
    private static final String COLUMN_EMAIL="email";
    private static final String COLUMN_PASSWORD="password";

    private static final String TABLE_APPOINTMENT="appointment";
    private static final String COLUMN_USER_ID="user_id";
    private static final String COLUMN_TIME="appointment_time";
    private static final String COLUMN_DAY="appointment_day";
    private static final String COLUMN_IS_TAKEN="false";

    // Create tables
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME+" TEXT, "
            + COLUMN_SURNAME+" TEXT, "
            + COLUMN_EMAIL+" TEXT, "
            + COLUMN_PASSWORD + " TEXT"
            + ")";

    private static final String CREATE_TABLE_APPOINTMENT = "CREATE TABLE " + TABLE_APPOINTMENT + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_ID + " TEXT,"
            + COLUMN_TIME+ " TEXT,"
            + COLUMN_DAY+ " TEXT,"
            + COLUMN_IS_TAKEN+ " TEXT"
            + ")";

    SQLiteDatabase database;

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_APPOINTMENT);
            generateAppointments(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENT);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public boolean RegisterUser(UserModel um){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(COLUMN_NAME, um.getName());
        cv.put(COLUMN_SURNAME,um.getSurname());
        cv.put(COLUMN_EMAIL,um.getEmail());
        cv.put(COLUMN_PASSWORD,um.getPassword());

        long insert=db.insert(TABLE_USER,null,cv);
        db.close();

        if(insert==-1) return false;
        else return true;
    }
    public Boolean checkUserEmail(String email){
        SQLiteDatabase info=this.getWritableDatabase();
        Cursor cursor=info.rawQuery("Select * from "+TABLE_USER+" where "+COLUMN_EMAIL+" =? ", new String[]{email});

        if(cursor.getCount()>0) return true;
        else return false;
    }
    @SuppressLint("Range")
    public int checkUserLogin(String email, String password){
        SQLiteDatabase info=this.getReadableDatabase();
        String[] columns={COLUMN_ID};
        String selection=COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs={email, password};

        Cursor cursor=info.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int userId = -1;

        if (cursor!=null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            cursor.close();
        }
        info.close();
        return userId;
    }
    @SuppressLint("Range")
    public UserModel getUserById(int userId) {
        SQLiteDatabase db=this.getReadableDatabase();
        String[] columns={COLUMN_ID, COLUMN_NAME, COLUMN_SURNAME, COLUMN_EMAIL};
        String selection=COLUMN_ID + " = ?";
        String[] selectionArgs={String.valueOf(userId)};

        Cursor cursor=db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        UserModel user=null;

        if (cursor!=null && cursor.moveToFirst()) {
            user = new UserModel();
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            user.setSurname(cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            cursor.close();
        }
        db.close();
        return user;
    }
    public boolean AddAppointment(AppointmentModel am, SQLiteDatabase db){
        ContentValues cv=new ContentValues();

        cv.put(COLUMN_USER_ID, am.getId());
        cv.put(COLUMN_TIME, am.getTime());
        cv.put(COLUMN_DAY, am.getDay());
        cv.put(COLUMN_IS_TAKEN, am.getIsTaken());

        long insert=db.insert(TABLE_APPOINTMENT,null,cv);

        if(insert==-1) return false;
        else return true;
    }
    public void generateAppointments(SQLiteDatabase db) {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        for (String day : days) {
            for (int i = 0; i < 5; i++) { // 5 appointments are available for each day
                String time = generateStartTime(i);
                String isTaken = "false";

                AppointmentModel appointment = new AppointmentModel();
                appointment.setUserId("000");
                appointment.setTime(time);
                appointment.setDay(day);
                appointment.setIsTaken(isTaken);

                AddAppointment(appointment, db);
            }
        }
    }

    private static String generateStartTime(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10 + offset);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(calendar.getTime());

    }
    public boolean isAppointmentTaken(int appointmentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_IS_TAKEN};
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(appointmentId)};

        Cursor cursor = db.query(TABLE_APPOINTMENT, columns, selection, selectionArgs, null, null, null);

        boolean isTaken = false;

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String takenValue = cursor.getString(cursor.getColumnIndex(COLUMN_IS_TAKEN));
            isTaken = takenValue.equals("true");
            cursor.close();
        }
        return isTaken;
    }
    public void updateAppointments() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENT);
        db.execSQL(CREATE_TABLE_APPOINTMENT);

        ContentValues cv = new ContentValues();

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        for (String day : days) {
            for (int i = 0; i < 5; i++) { // 5 appointments are available for each day
                String time = generateStartTime(i);
                String isTaken = "false";

                AppointmentModel appointment = new AppointmentModel();
                appointment.setUserId("000");
                appointment.setTime(time);
                appointment.setDay(day);
                appointment.setIsTaken(isTaken);

                AddAppointment(appointment, db);
            }
        }

        db.close();
    }
    public boolean bookAppointmentForUser(int userId, int appointmentId){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_USER_ID, userId);
        cv.put(COLUMN_IS_TAKEN, "true");

        long result = db.update(TABLE_APPOINTMENT, cv, COLUMN_ID + "=?", new String[]{String.valueOf(appointmentId)});
        db.close();

        return result != -1;
    }
    public List<AppointmentModel> getAppointmentsForUser(int userId) {
        List<AppointmentModel> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                TABLE_APPOINTMENT + "." + COLUMN_TIME + ", " +
                TABLE_APPOINTMENT + "." + COLUMN_DAY + ", " +
                TABLE_APPOINTMENT + "." + COLUMN_IS_TAKEN + ", " +
                TABLE_APPOINTMENT + "." + COLUMN_ID + ", " +
                TABLE_APPOINTMENT + "." + COLUMN_USER_ID +
                " FROM " + TABLE_APPOINTMENT +
                " WHERE " + TABLE_APPOINTMENT + "." + COLUMN_USER_ID + " = ?"+
                " ORDER BY " + TABLE_APPOINTMENT + "." + COLUMN_TIME + " ASC";
        Cursor cursor=db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String uId = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                @SuppressLint("Range") String day = cursor.getString(cursor.getColumnIndex(COLUMN_DAY));
                @SuppressLint("Range") String isTaken = cursor.getString(cursor.getColumnIndex(COLUMN_IS_TAKEN));
                @SuppressLint("Range") int id=Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                AppointmentModel appointmentModel = new AppointmentModel(id, uId, time, day, isTaken);
                appointments.add(appointmentModel);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return appointments;
    }
}