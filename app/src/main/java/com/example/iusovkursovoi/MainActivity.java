package com.example.iusovkursovoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnAppointment;
    private Button btnDrugIntake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAppointment = findViewById(R.id.btnAppointment);
        btnDrugIntake = findViewById(R.id.btnDrugIntake);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("base.db",MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS DrugIntakes (id Integer PRIMARY KEY, medicine Text, date Text, timespan Integer, count Integer)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Appointments (id Integer PRIMARY KEY, specialization Text, doctor Text, date Text, cabinet Text, note Text)");
        btnDrugIntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListDrugIntake.class);
                startActivity(intent);
            }
        });
        btnAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListAppointments.class);
                startActivity(intent);
            }
        });
    }
}