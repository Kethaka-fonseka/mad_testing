package com.example.my1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.net.Inet4Address;

public class display extends AppCompatActivity {

    TextView id,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        id=findViewById(R.id.viewID);
        name=findViewById(R.id.viewName);

        Intent intent=getIntent();
User u1=intent.getParcelableExtra("std");

id.setText(u1.getID());
name.setText(u1.getName());


    }
}