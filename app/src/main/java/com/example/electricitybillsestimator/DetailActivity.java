package com.example.electricitybillsestimator;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView txtMonth = findViewById(R.id.txtDetailMonth);
        TextView txtUnit = findViewById(R.id.txtDetailUnit);
        TextView txtTotal = findViewById(R.id.txtDetailTotal);
        TextView txtRebate = findViewById(R.id.txtDetailRebate);
        TextView txtFinal = findViewById(R.id.txtDetailFinal);

        // Receive data from intent
        String month = getIntent().getStringExtra("month");
        double unit = getIntent().getDoubleExtra("unit", 0);
        double total = getIntent().getDoubleExtra("total", 0);
        double rebate = getIntent().getDoubleExtra("rebate", 0);
        double finalCost = getIntent().getDoubleExtra("final", 0);

        // Display details
        txtMonth.setText("Month: " + month);
        txtUnit.setText("Unit Used: " + unit + " kWh");
        txtTotal.setText(String.format("Total Charges: RM %.2f", total));
        txtRebate.setText("Rebate: " + rebate + "%");
        txtFinal.setText(String.format("Final Cost: RM %.2f", finalCost));
    }
}
