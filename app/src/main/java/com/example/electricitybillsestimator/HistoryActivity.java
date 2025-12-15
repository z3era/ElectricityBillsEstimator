package com.example.electricitybillsestimator;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView listViewBills;
    DBHelper dbHelper;
    ArrayList<String> billList;
    Cursor cursor; // keep cursor accessible for click

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listViewBills = findViewById(R.id.listViewBills);
        dbHelper = new DBHelper(this);
        billList = new ArrayList<>();

        loadBills();
    }

    private void loadBills() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery(
                "SELECT id, month, unit, total, rebate, final FROM bills",
                null
        );

        billList.clear();

        if (cursor.moveToFirst()) {
            do {
                String month = cursor.getString(1);
                double finalCost = cursor.getDouble(5);

                billList.add(month + "  -  RM " + String.format("%.2f", finalCost));
            } while (cursor.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                billList
        );

        listViewBills.setAdapter(adapter);

        // CLICK LIST ITEM → OPEN DETAIL PAGE
        listViewBills.setOnItemClickListener((parent, view, position, id) -> {

            cursor.moveToPosition(position);

            Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
            intent.putExtra("month", cursor.getString(1));
            intent.putExtra("unit", cursor.getDouble(2));
            intent.putExtra("total", cursor.getDouble(3));
            intent.putExtra("rebate", cursor.getDouble(4));
            intent.putExtra("final", cursor.getDouble(5));

            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }
}
