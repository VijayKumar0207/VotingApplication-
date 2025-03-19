package com.example.votingapplication;

import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Adminlog extends AppCompatActivity {

    Button b1,b2;
    EditText e1,e2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlog);
        b1=findViewById(R.id.ab1);
        b2=findViewById(R.id.ab2);
        e1=findViewById(R.id.ae1);
        e2=findViewById(R.id.ae2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = e1.getText().toString();
                String pass = e2.getText().toString();
                if(user.equals("vijay") && pass.equals("123")) {
                    Intent in = new Intent(Adminlog.this, Admin2pg.class);
                    startActivity(in);
                }else {
                    Toast t = makeText(Adminlog.this,"Invalid Username and Password",Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inn = new Intent(Adminlog.this, Welcomepg.class);
                startActivity(inn);
            }
        });

    }
}