package com.example.votingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin2pg extends AppCompatActivity {

    Button vv, back, sb, rsb;
    EditText ee1, ee2, ee3;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin2pg);

        vv = findViewById(R.id.vb1);
        back = findViewById(R.id.cb2);
        sb = findViewById(R.id.sb1);
        rsb = findViewById(R.id.rs);
        ee1 = findViewById(R.id.ce1);
        ee2 = findViewById(R.id.ce2);
        ee3 = findViewById(R.id.ce3);

        dbRef = FirebaseDatabase.getInstance().getReference();

        loadCandidates();

        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCandidates();
            }
        });

        rsb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetConfirmation();
            }
        });

        vv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Resultpg.class);
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Admin2pg.this, Welcomepg.class);
                startActivity(in);
            }
        });
    }

    private void loadCandidates() {
        dbRef.child("Candidates").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String candi1 = snapshot.child("Candidate1").getValue(String.class);
                    String candi2 = snapshot.child("Candidate2").getValue(String.class);
                    String candi3 = snapshot.child("Candidate3").getValue(String.class);

                    ee1.setText(candi1);
                    ee2.setText(candi2);
                    ee3.setText(candi3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Admin2pg.this, "Error loading candidates", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveCandidates() {
        String candi1 = ee1.getText().toString().trim();
        String candi2 = ee2.getText().toString().trim();
        String candi3 = ee3.getText().toString().trim();

        if (candi1.isEmpty() || candi2.isEmpty() || candi3.isEmpty()) {
            Toast.makeText(Admin2pg.this, "Enter all candidate names", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.child("Candidates").child("Candidate1").setValue(candi1);
        dbRef.child("Candidates").child("Candidate2").setValue(candi2);
        dbRef.child("Candidates").child("Candidate3").setValue(candi3).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Admin2pg.this, "Candidates Updated!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Admin2pg.this, "Error updating candidates!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showResetConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Reset")
                .setMessage("Are you sure you want to reset votes, candidates, and user data? This action cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> resetElection())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void resetElection() {
        dbRef.child("votes").setValue(null).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Reset vote counters
                dbRef.child("votes").child("Candidate1").setValue(0);
                dbRef.child("votes").child("Candidate2").setValue(0);
                dbRef.child("votes").child("Candidate3").setValue(0);
                dbRef.child("votes").child("totalVotes").setValue(0);

                // Clear candidates
                dbRef.child("Candidates").setValue(null).addOnCompleteListener(candidateClearTask -> {
                    if (candidateClearTask.isSuccessful()) {
                        // Clear user login information
                        dbRef.child("Users").setValue(null).addOnCompleteListener(userClearTask -> {
                            if (userClearTask.isSuccessful()) {
                                ee1.setText("");
                                ee2.setText("");
                                ee3.setText("");
                                Toast.makeText(Admin2pg.this, "All data reset successfully!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Admin2pg.this, "Votes & candidates reset, but failed to clear user data!", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(Admin2pg.this, "Votes reset but failed to clear candidates!", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(Admin2pg.this, "Error resetting election!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
