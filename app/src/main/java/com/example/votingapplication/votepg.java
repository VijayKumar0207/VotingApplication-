package com.example.votingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class votepg extends AppCompatActivity {

    Button can1, can2, can3;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votepg);
        can1 = findViewById(R.id.cb1);
        can2 = findViewById(R.id.cb2);
        can3 = findViewById(R.id.cb3);
        dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.child("Candidates").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String candi1 = snapshot.child("Candidate1").getValue(String.class);
                    String candi2 = snapshot.child("Candidate2").getValue(String.class);
                    String candi3 = snapshot.child("Candidate3").getValue(String.class);
                    can1.setText(candi1 != null ? candi1 : "Candidate 1");
                    can2.setText(candi2 != null ? candi2 : "Candidate 2");
                    can3.setText(candi3 != null ? candi3 : "Candidate 3");
                } else {
                    Toast.makeText(votepg.this, "No candidates found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(votepg.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        can1.setOnClickListener(v -> castVote("Candidate1"));
        can2.setOnClickListener(v -> castVote("Candidate2"));
        can3.setOnClickListener(v -> castVote("Candidate3"));
    }

    private void castVote(String candidate) {
        // First increment candidate votes
        dbRef.child("votes").child(candidate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long currentVotes = snapshot.exists() ? snapshot.getValue(Long.class) : 0;
                dbRef.child("votes").child(candidate).setValue(currentVotes + 1)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Now increment total votes
                                incrementTotalVotes();
                            } else {
                                Toast.makeText(votepg.this, "Error casting vote!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(votepg.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void incrementTotalVotes() {
        dbRef.child("votes").child("totalVotes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long totalVotes = snapshot.exists() ? snapshot.getValue(Long.class) : 0;
                dbRef.child("votes").child("totalVotes").setValue(totalVotes + 1)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(votepg.this, "Vote Casted Successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(votepg.this, Welcomepg.class));
                                finish();  // Close current page
                            } else {
                                Toast.makeText(votepg.this, "Error updating total votes!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(votepg.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
