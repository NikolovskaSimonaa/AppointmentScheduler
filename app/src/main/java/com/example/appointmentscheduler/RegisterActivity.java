package com.example.appointmentscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHandler(this);

        editTextName= findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
    }

    public void registerButtonClicked(View view){
        String n = editTextName.getText().toString().trim();
        String s = editTextSurname.getText().toString().trim();
        String e = editTextEmail.getText().toString().trim();
        String p = editTextPassword.getText().toString().trim();
        UserModel um;
        if (e.isEmpty() && p.isEmpty() && n.isEmpty() && s.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "In order to register fill in all the fields", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseHandler databaseHandler=new DatabaseHandler(RegisterActivity.this);
            if(databaseHandler.checkUserEmail(e)==true){
                Toast.makeText(RegisterActivity.this, "User already exists.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    um = new UserModel(-1, n,s, e, p);
                    boolean success = databaseHandler.RegisterUser(um);
                    if(success){
                        Toast.makeText(RegisterActivity.this, "User added successfully.", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } catch (Exception exception) {
                    Toast.makeText(RegisterActivity.this, "Error creating user,try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void goToLogin(View view){
        Intent newActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(newActivityIntent);
    }
}