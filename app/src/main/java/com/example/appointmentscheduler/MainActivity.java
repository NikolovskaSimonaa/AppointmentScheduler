package com.example.appointmentscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
    }
    public void loginButtonClicked(View view){
        String email = editTextEmail.getText().toString().trim();
        String pass = editTextPassword.getText().toString().trim();
        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        } else {
            Boolean checkemail=db.checkUserEmail(email);
            if(checkemail==false) {
                Toast.makeText(MainActivity.this, "You don't have an account. Please register first.", Toast.LENGTH_SHORT).show();
            }else {
                int checkLoginid = db.checkUserLogin(email, pass);
                if (checkLoginid == -1) {
                    Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                } else  { //the email and password are correct
                    Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                    intent.putExtra("USER_ID", checkLoginid); //pass the signed user_id to the home screen
                    startActivity(intent);
                }
            }
        }
    }
    public void goToRegister(View view){
        Intent newActivityIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(newActivityIntent);
    }
}