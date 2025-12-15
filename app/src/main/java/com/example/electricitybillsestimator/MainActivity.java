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

        // Initialize database helper
        dbHelper = new DBHelper(this);

        // Link UI components
        spinnerMonth = findViewById(R.id.spinnerMonth);
        edtUnit = findViewById(R.id.edtUnit);
        radioRebate = findViewById(R.id.radioRebate);
        txtTotal = findViewById(R.id.txtTotal);
        txtFinal = findViewById(R.id.txtFinal);

        // Spinner data (months)
        String[] months = {
                "Select Month",
                "January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                months
        );

        spinnerMonth.setAdapter(adapter);
    }

    // Button onClick method
    public void calculateBill(View view) {

        String month = spinnerMonth.getSelectedItem().toString();

        // Validation
        if (month.equals("Select Month")) {
            Toast.makeText(this, "Please select a month", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edtUnit.getText().toString().isEmpty()) {
            edtUnit.setError("Please enter electricity unit (kWh)");
            return;
        }

        if (radioRebate.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select rebate percentage", Toast.LENGTH_SHORT).show();
            return;
        }

        double unit = Double.parseDouble(edtUnit.getText().toString());

        // Get selected rebate
        int selectedId = radioRebate.getCheckedRadioButtonId();
        RadioButton selectedRebate = findViewById(selectedId);

        double rebate = Double.parseDouble(
                selectedRebate.getText().toString().replace("%", "")
        ) / 100;

        // Electricity block calculation
        double totalCharges;

        if (unit <= 200) {
            totalCharges = unit * 0.218;
        } else if (unit <= 300) {
            totalCharges = (200 * 0.218) + ((unit - 200) * 0.334);
        } else if (unit <= 600) {
            totalCharges = (200 * 0.218) + (100 * 0.334)
                    + ((unit - 300) * 0.516);
        } else {
            totalCharges = (200 * 0.218) + (100 * 0.334)
                    + (300 * 0.516) + ((unit - 600) * 0.546);
        }

        // Final cost after rebate
        double finalCost = totalCharges - (totalCharges * rebate);

        // Display results
        txtTotal.setText(String.format("Total Charges: RM %.2f", totalCharges));
        txtFinal.setText(String.format("Final Cost: RM %.2f", finalCost));

        // Save to SQLite database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("month", month);
        values.put("unit", unit);
        values.put("total", totalCharges);
        values.put("rebate", rebate * 100); // store as %
        values.put("final", finalCost);

        db.insert("bills", null, values);

        Toast.makeText(this, "Bill saved successfully", Toast.LENGTH_SHORT).show();
    }
    public void openHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }
    public void openAbout(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }

}