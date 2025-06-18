package com.example.expanse_track;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends AppCompatActivity {
    TextView showId, showAmount, showDesc, showDate, showType;
    Button btnDashboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        showId = findViewById(R.id.show_id);
        showDesc = findViewById(R.id.show_desc);
        showAmount = findViewById(R.id.show_amount);
        showDate = findViewById(R.id.show_date);
        showType = findViewById(R.id.show_type);
        btnDashboard = findViewById(R.id.btn_dashboard);

        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboard();
            }
        });
        Intent intentShow = getIntent();

        String strid = intentShow.getStringExtra("id");
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String dt = ("SELECT `amount`, `description`, `date`, `type` FROM `transaction` WHERE id ='" + strid + "'");
        Cursor data = db.rawQuery(dt, new String[0]);

        if (data.moveToFirst()) {
            String stramount = data.getString(0);
            String description = data.getString(1);
            String date = data.getString(2);
            String strtype = data.getString(3);

            // Masukkan data ke dalam EditText atau TextView
            showId.setText(strid);
            showId.setVisibility(View.GONE);
            showDesc.setText(String.format("Desc = %s", description));
            showAmount.setText(String.format("Amount = %s", stramount));
            showDate.setText(String.format("Date = %s", date));
            showType.setText(String.format("Type = %s", strtype));

        }
    }
    private void dashboard() {
        Intent goDashboard = new Intent(this, MainActivity.class);
        startActivity(goDashboard);
    }

}