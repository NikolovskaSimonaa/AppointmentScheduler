package com.example.appointmentscheduler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ClassViewHolder> {
    private Context context;
    DatabaseHandler databaseHandler;
    private List<AppointmentModel> appointmentList;
    int userId;
    public AppointmentAdapter(Context context, List<AppointmentModel> appointmentList,DatabaseHandler databaseHandler, int userId) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.databaseHandler = databaseHandler;
        this.userId = userId;
    }
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_row, parent, false);
        return new ClassViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        AppointmentModel currentAppointment = appointmentList.get(position);

        holder.day.setText(currentAppointment.getDay());
        holder.time.setText(currentAppointment.getTime());
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }
    static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView day;
        TextView time;
        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.appointmentDay);
            time = itemView.findViewById(R.id.appointmentTime);
        }
    }
}
