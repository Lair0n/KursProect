package com.example.iusovkursovoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class ListDrugIntake extends AppCompatActivity {

    private ImageButton btnBack;
    private LinearLayout container;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_drug_intake);
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
                Intent intent = new Intent(ListDrugIntake.this, AddDrugIntake.class);
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
        Cursor query = db.rawQuery("SELECT id, medicine, date, timespan, count FROM DrugIntakes ORDER BY date",null);
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

            LinearLayout dateLayout = new LinearLayout(this);
            dateLayout.setLayoutParams(params);
            dateLayout.setGravity(Gravity.CENTER);
            dateLayout.setOrientation(LinearLayout.HORIZONTAL);
            layout.addView(dateLayout);

            TextView dateView = new TextView(this);
            dateView.setText("Прием ");
            dateView.setLayoutParams(dateParams);
            dateView.setTextSize(20);
            dateView.setTextColor(getResources().getColor(R.color.white));
            dateLayout.addView(dateView);

            TextView dateView1 = new TextView(this);
            dateView1.setText(formatDate(query.getString(2)));
            dateView1.setLayoutParams(dateParams);
            dateView1.setTextSize(20);
            dateView1.setTextColor(getResources().getColor(R.color.white));
            dateLayout.addView(dateView1);

            TextView medicineView = new TextView(this);
            medicineView.setText("Лекарство: "+query.getString(1));
            params.setMargins(0,convertPixelInDp(16),0,0);
            medicineView.setLayoutParams(params);
            medicineView.setTextSize(19);
            medicineView.setTextColor(getResources().getColor(R.color.white));
            layout.addView(medicineView);

            TextView timeSpanView = new TextView(this);
            timeSpanView.setId(query.getInt(3));
            timeSpanView.setText("Промежуток между днями: "+query.getInt(3));
            params.setMargins(0,convertPixelInDp(16),0,0);
            timeSpanView.setLayoutParams(params);
            timeSpanView.setTextSize(19);
            timeSpanView.setTextColor(getResources().getColor(R.color.white));
            layout.addView(timeSpanView);

            TextView countView = new TextView(this);
            countView.setText("Количество приемов в день: " + query.getInt(4));
            params.setMargins(0,convertPixelInDp(16),0,0);
            countView.setLayoutParams(params);
            countView.setTextSize(19);
            countView.setTextColor(getResources().getColor(R.color.white));
            layout.addView(countView);

            Button btnConfirm = new Button(this);
            btnConfirm.setAllCaps(false);
            btnConfirm.setId(query.getInt(0));
            btnConfirm.setText("Подтвердить прием");
            btnConfirm.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.execSQL("UPDATE DrugIntakes SET date = '"+updateDate(dateView1.getText().toString(),timeSpanView.getId())+"' WHERE id = '"+btnConfirm.getId()+"'");
                    Toast.makeText(getBaseContext(),"Прием подтвержден.", Toast.LENGTH_LONG).show();
                    loadActivity();
                }
            });
            layout.addView(btnConfirm);
            Button btnDelete = new Button(this);
            btnDelete.setAllCaps(false);
            btnDelete.setId(query.getInt(0));
            btnDelete.setText("Удалить прием");
            btnDelete.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.execSQL("DELETE FROM DrugIntakes WHERE id = '"+btnDelete.getId()+"'");
                    Toast.makeText(getBaseContext(),"Прием удален.", Toast.LENGTH_LONG).show();
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter stringFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return (LocalDate.parse(dateString, formatter)).format(stringFormatter);
    }
    private String updateDate(String dateString, int days){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter stringFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(dateString, stringFormatter);
        date = date.plusDays(days);
        return date.format(formatter);
    }
}