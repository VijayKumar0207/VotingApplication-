package com.example.votingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Resultpg extends AppCompatActivity {
    DatabaseReference dbRef;
    Button bb1;
    TextView tc1, tc2, tc3, tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultpg);
        tc1 = findViewById(R.id.t1);
        tc2 = findViewById(R.id.t2);
        tc3 = findViewById(R.id.t3);
        tv = findViewById(R.id.tt);
        bb1 = findViewById(R.id.b1);
        dbRef = FirebaseDatabase.getInstance().getReference("votes");

        fetchResults();

        bb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Resultpg.this, Welcomepg.class);
                startActivity(i);
            }
        });
    }

    private void fetchResults() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long votes1 = snapshot.child("Candidate1").exists() ? snapshot.child("Candidate1").getValue(Long.class) : 0;
                    long votes2 = snapshot.child("Candidate2").exists() ? snapshot.child("Candidate2").getValue(Long.class) : 0;
                    long votes3 = snapshot.child("Candidate3").exists() ? snapshot.child("Candidate3").getValue(Long.class) : 0;


                    long totalVotes = votes1 + votes2 + votes3;

                    tc1.setText("Candidate 1: " + votes1 + " votes");
                    tc2.setText("Candidate 2: " + votes2 + " votes");
                    tc3.setText("Candidate 3: " + votes3 + " votes");
                    tv.setText("Total Votes: " + totalVotes);

                    dbRef.child("totalVotes").setValue(totalVotes);
                } else {
                    tc1.setText("Candidate 1: 0 votes");
                    tc2.setText("Candidate 2: 0 votes");
                    tc3.setText("Candidate 3: 0 votes");
                    tv.setText("Total Votes: 0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Resultpg.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
