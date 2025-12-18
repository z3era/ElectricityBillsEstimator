package com.example.electricitybillsestimator;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int billId = getIntent().getIntExtra("id", -1);

        ((TextView)findViewById(R.id.txtDetailMonth))
                .setText("Month: " + getIntent().getStringExtra("month"));

        ((TextView)findViewById(R.id.txtDetailUnit))
                .setText("Unit Used: " + getIntent().getDoubleExtra("unit",0));

        ((TextView)findViewById(R.id.txtDetailTotal))
                .setText("Total Charges: RM " + getIntent().getDoubleExtra("total",0));

        ((TextView)findViewById(R.id.txtDetailRebate))
                .setText("Rebate: " + getIntent().getDoubleExtra("rebate",0) + "%");

        ((TextView)findViewById(R.id.txtDetailFinal))
                .setText("Final Cost: RM " + getIntent().getDoubleExtra("final",0));

        Button btnDelete = findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(v -> {
            SQLiteDatabase db = new DBHelper(this).getWritableDatabase();
            db.delete("bills", "id=?", new String[]{String.valueOf(billId)});
            Toast.makeText(this, "Bill deleted", Toast.LENGTH_SHORT).show();
            finish();
        });

    }
}
