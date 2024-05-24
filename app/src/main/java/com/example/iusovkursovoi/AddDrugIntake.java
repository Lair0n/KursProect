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

public class AddDrugIntake extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etMedicine;
    private EditText etDate;
    private EditText etTimeSpan;
    private EditText etCount;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug_intake);
        btnBack = findViewById(R.id.btnBack);
        etMedicine = findViewById(R.id.etMedicine);
        etDate = findViewById(R.id.etDate);
        etTimeSpan = findViewById(R.id.etTimeSpan);
        etCount = findViewById(R.id.etCount);
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
                try{
                    String date = getDate(etDate.getText().toString());
                    if(etMedicine.getText().toString().isEmpty() | Integer.parseInt(etTimeSpan.getText().toString()) < 1 | date.isEmpty() | Integer.parseInt(etCount.getText().toString()) < 1){
                        Toast.makeText(getBaseContext(),"Не заполнены обязательные данные.",Toast.LENGTH_LONG).show();
                        return;
                    }
                    SQLiteDatabase db = getBaseContext().openOrCreateDatabase("base.db", MODE_PRIVATE,null);
                    db.execSQL("INSERT OR IGNORE INTO DrugIntakes VALUES (NULL,'"+ etMedicine.getText().toString() +"', '"+date+"', '"+Integer.parseInt(etTimeSpan.getText().toString())+"', '"+Integer.parseInt(etCount.getText().toString())+"')");
                    finish();
                }catch (Exception e){
                    Toast.makeText(getBaseContext(),"Некорректные данные.",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private String getDate(String dateString){//получение текущей даты в формате
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter stringFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try{
            return (LocalDate.parse(dateString, stringFormatter)).format(formatter);
        }catch (Exception e){
            return "";
        }
    }
}