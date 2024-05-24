package com.example.iusovkursovoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ListAppointments extends AppCompatActivity {

    private ImageButton btnBack;
    private LinearLayout container;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_appointments);
        btnBack = findViewById(R.id.btnBack);
        container = findViewById(R.id.container);
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
                Intent intent = new Intent(ListAppointments.this, AddAppointment.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        loadActivity();
    }

    private void loadActivity(){
        container.removeAllViews();
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("base.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT id, specialization, doctor, date, cabinet, note FROM Appointments ORDER BY date",null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dateParams.gravity = Gravity.CENTER;
        while(query.moveToNext()){
            LinearLayout layout = new LinearLayout(this);
            params.setMargins(0,convertPixelInDp(10),0,0);
            layout.setLayoutParams(params);
            layout.setPadding(convertPixelInDp(10),convertPixelInDp(8),convertPixelInDp(10),convertPixelInDp(16));
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setBackgroundResource(R.drawable.block_bg);
            container.addView(layout);
            TextView dateView = new TextView(this);
            dateView.setText("Запись на "+formatDate(query.getString(3)));
            dateView.setLayoutParams(dateParams);
            dateView.setTextSize(20);
            dateView.setTextColor(getResources().getColor(R.color.white));
            layout.addView(dateView);
            TextView descriptionView = new TextView(this);
            descriptionView.setText("Врач: "+query.getString(2)+ "\nСпециальность: "+query.getString(1)+"\nКабинет: "+query.getString(4) + "\nЗаметка: " + query.getString(5));
            params.setMargins(0,convertPixelInDp(16),0,0);
            descriptionView.setLayoutParams(params);
            descriptionView.setTextSize(19);
            descriptionView.setTextColor(getResources().getColor(R.color.white));
            layout.addView(descriptionView);
            Button btnDelete = new Button(this);
            btnDelete.setAllCaps(false);
            btnDelete.setId(query.getInt(0));
            btnDelete.setText("Удалить запись");
            btnDelete.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.execSQL("DELETE FROM Appointments WHERE id = '"+btnDelete.getId()+"'");
                    Toast.makeText(getBaseContext(),"Запись удалена.", Toast.LENGTH_LONG).show();
                    loadActivity();
                }
            });
            layout.addView(btnDelete);
        }
    }

    private int convertPixelInDp(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14f, getResources().getDisplayMetrics());
    }

    private String formatDate(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
        DateTimeFormatter stringFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm");
        return (LocalDateTime.parse(dateString, formatter)).format(stringFormatter);
    }
}