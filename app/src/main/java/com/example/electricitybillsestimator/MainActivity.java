package com.example.electricitybillsestimator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth;
    EditText edtUnit;
    RadioGroup radioRebate;
    TextView txtTotal, txtFinal;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        edtUnit = findViewById(R.id.edtUnit);
        radioRebate = findViewById(R.id.radioRebate);
        txtTotal = findViewById(R.id.txtTotal);
        txtFinal = findViewById(R.id.txtFinal);

        String[] months = {
                "Select Month",
                "January","February","March","April","May","June",
                "July","August","September","October","November","December"
        };

        spinnerMonth.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                months
        ));
    }

    public void calculateBill(View view) {

        String month = spinnerMonth.getSelectedItem().toString();

        if (month.equals("Select Month")) {
            Toast.makeText(this, "Please select a month", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edtUnit.getText().toString().isEmpty()) {
            edtUnit.setError("Please enter electricity unit");
            return;
        }

        if (radioRebate.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Select rebate", Toast.LENGTH_SHORT).show();
            return;
        }

        double unit = Double.parseDouble(edtUnit.getText().toString());

        RadioButton selected = findViewById(radioRebate.getCheckedRadioButtonId());
        double rebate = Double.parseDouble(
                selected.getText().toString().replace("%","")
        ) / 100;

        double total;
        if (unit <= 200) total = unit * 0.218;
        else if (unit <= 300) total = 200*0.218 + (unit-200)*0.334;
        else if (unit <= 600) total = 200*0.218 + 100*0.334 + (unit-300)*0.516;
        else total = 200*0.218 + 100*0.334 + 300*0.516 + (unit-600)*0.546;

        double finalCost = total - (total * rebate);

        txtTotal.setText(String.format("Total Charges: RM %.2f", total));
        txtFinal.setText(String.format("Final Cost: RM %.2f", finalCost));

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("month", month);
        cv.put("unit", unit);
        cv.put("total", total);
        cv.put("rebate", rebate * 100);
        cv.put("final", finalCost);

        db.insert("bills", null, cv);
        Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
    }

    public void openHistory(View v) {
        startActivity(new Intent(this, HistoryActivity.class));
    }

    public void openAbout(View v) {
        startActivity(new Intent(this, AboutActivity.class));
    }
}