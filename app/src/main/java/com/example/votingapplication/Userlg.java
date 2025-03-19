package com.example.votingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Userlg extends AppCompatActivity {

    EditText et1, et2, et3; // et3 for DOB
    Button b1, b2;
    DatabaseReference databaseReference;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlg);

        b1 = findViewById(R.id.ub1);
        b2 = findViewById(R.id.ub2);
        et1 = findViewById(R.id.ue1);
        et2 = findViewById(R.id.ue2);
        et3 = findViewById(R.id.ue3);  // Date of Birth field
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        calendar = Calendar.getInstance();

        et3.setOnClickListener(v -> showDatePicker());

        b1.setOnClickListener(v -> {
            String name = et1.getText().toString().trim();
            String password = et2.getText().toString().trim();
            String dob = et3.getText().toString().trim();

            if (name.isEmpty() || password.isEmpty() || dob.isEmpty()) {
                Toast.makeText(Userlg.this, "Please enter Name, Password, and Date of Birth", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.matches("\\d{10}")) {
                Toast.makeText(Userlg.this, "Password must be exactly 10 digits", Toast.LENGTH_SHORT).show();
                return;
            }

            int age = calculateAge(dob);
            if (age < 18) {
                Toast.makeText(Userlg.this, "You are under 18. Not allowed to vote!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Userlg.this, Welcomepg.class));
                finish();
            } else {
                checkUserEntry(name, password);
            }
        });

        b2.setOnClickListener(v -> {
            Intent in = new Intent(Userlg.this, Welcomepg.class);
            startActivity(in);
        });
    }

    private void showDatePicker() {
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Userlg.this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
            et3.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private int calculateAge(String dobString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date dobDate = sdf.parse(dobString);
            Calendar dobCalendar = Calendar.getInstance();
            dobCalendar.setTime(dobDate);

            Calendar today = Calendar.getInstance();

            int age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            return age;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Userlg.this, "Invalid date format!", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    private void checkUserEntry(String name, String password) {
        databaseReference.child(password).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(Userlg.this, "You have already voted", Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child(password).setValue(name);
                    Toast.makeText(Userlg.this, "Entry successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Userlg.this, votepg.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Userlg.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
