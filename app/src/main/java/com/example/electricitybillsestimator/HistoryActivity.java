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
    Cursor cursor;

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
        cursor = db.rawQuery("SELECT * FROM bills", null);

        billList.clear();

        while (cursor.moveToNext()) {
            billList.add(
                    cursor.getString(1) +
                            " - RM " +
                            String.format("%.2f", cursor.getDouble(5))
            );
        }

        listViewBills.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                billList
        ));

        listViewBills.setOnItemClickListener((p, v, pos, id) -> {
            cursor.moveToPosition(pos);

            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra("id", cursor.getInt(0));
            i.putExtra("month", cursor.getString(1));
            i.putExtra("unit", cursor.getDouble(2));
            i.putExtra("total", cursor.getDouble(3));
            i.putExtra("rebate", cursor.getDouble(4));
            i.putExtra("final", cursor.getDouble(5));
            startActivity(i);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
    }
}
