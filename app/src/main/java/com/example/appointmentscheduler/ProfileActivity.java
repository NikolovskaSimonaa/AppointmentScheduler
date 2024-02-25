package com.example.appointmentscheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    DatabaseHandler db;
    private TextView name, surname, email;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = new DatabaseHandler(this);
        userId = getIntent().getIntExtra("USER_ID", -1);
        setupUILogic();
    }
    private void setupUILogic(){
        name = findViewById(R.id.nameField);
        surname = findViewById(R.id.surnameField);
        email = findViewById(R.id.emailField);
        if (userId != -1) {
            UserModel user = db.getUserById(userId);
            if (user != null) {
                name.setText(user.getName());
                surname.setText(user.getSurname());
                email.setText(user.getEmail());

            } else {
                Toast.makeText(ProfileActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ProfileActivity.this, "Invalid user ID", Toast.LENGTH_SHORT).show();
        }
    }

    public void Logout(View view){
        Intent newActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(newActivityIntent);
    }
}