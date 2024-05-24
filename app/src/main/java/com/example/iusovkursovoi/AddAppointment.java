package com.example.iusovkursovoi;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddAppointment extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etSpecialization;
    private EditText etDoctor;
    private EditText etDate;
    private EditText etCabinet;
    private EditText etNote;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        btnBack = findViewById(R.id.btnBack);
        etSpecialization = findViewById(R.id.etSpecialization);
        etDoctor = findViewById(R.id.etDoctor);
        etDate = findViewById(R.id.etDate);
        etCabinet = findViewById(R.id.etCabinet);
        etNote = findViewById(R.id.etNote);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = getDate(etDate.getText().toString());
                if(etSpecialization.getText().toString().isEmpty() | etDoctor.getText().toString().isEmpty() | date.isEmpty() | etCabinet.getText().toString().isEmpty()){
                    Toast.makeText(getBaseContext(),"Не заполнены обязательные данные.",Toast.LENGTH_LONG).show();
                    return;
                }
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("base.db", MODE_PRIVATE,null);
                db.execSQL("INSERT OR IGNORE INTO Appointments VALUES (NULL,'"+ etSpecialization.getText().toString() +"', '"+etDoctor.getText().toString()+"', '"+date+"', '"+etCabinet.getText().toString()+"', '"+etNote.getText().toString()+"')");
                finish();
            }
        });
    }

    private String getDate(String dateString){//получение текущей даты в формате
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
        DateTimeFormatter stringFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm");
        try{
            return (LocalDateTime.parse(dateString, stringFormatter)).format(formatter);
        }catch (Exception e){
            return "";
        }
    }
}