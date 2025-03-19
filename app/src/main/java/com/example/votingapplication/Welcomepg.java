package com.example.votingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Welcomepg extends AppCompatActivity {
    Button b1, b2;
    TextView tv;
    ImageButton aboutbtn;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomepg);
        tv = findViewById(R.id.tt);
        b1 = findViewById(R.id.bb1);
        b2 = findViewById(R.id.bb2);
        aboutbtn = findViewById(R.id.aboutBtn);

        dbRef = FirebaseDatabase.getInstance().getReference("votes/totalVotes");
        fetchTotalVotes();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Welcomepg.this, Adminlog.class);
                startActivity(i);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Welcomepg.this, Userlg.class);
                startActivity(i);
            }
        });
        ImageButton aboutBtn = findViewById(R.id.aboutBtn);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Welcomepg.this, AboutActivity.class));
            }
        } );


    }
    private void fetchTotalVotes() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long totalVotes = snapshot.getValue(Long.class) != null ? snapshot.getValue(Long.class) : 0;
                    tv.setText("Total Votes: " + totalVotes);
                } else {
                    tv.setText("Total Votes: 0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Welcomepg.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
