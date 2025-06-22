package com.example.expanse_track;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends AppCompatActivity {
    TextView showId, showAmount, showDesc, showDate, tvType;
    ImageButton btnDashboard;
    Button btnDlt, btnEdt;
    ImageView ivIcon;
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
        btnDashboard = findViewById(R.id.btn_dashboard);
        btnDlt = findViewById(R.id.btn_dlt);
        btnEdt = findViewById(R.id.btn_edt);

        ivIcon = findViewById(R.id.iv_icon);
        tvType = findViewById(R.id.tv_type);
        btnDlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        btnEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });
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
            showDesc.setText(String.format("Description:  %s", description));
            showAmount.setText(String.format("Amount: %s", stramount));
            showDate.setText(String.format("Date: %s", date));

            int type = Integer.parseInt(strtype);
            ivIcon.setImageResource(type == 0 ? R.drawable.baseline_arrow_downward_24 : R.drawable.baseline_arrow_upward_24);
            tvType.setText(type == 0 ? "Expense" : "Income");

        }
    }
    private void dashboard() {
//        Intent goDashboard = new Intent(this, MainActivity.class);
//        startActivity(goDashboard);
        finish();
    }
    private void edit() {
        Intent goEdit = new Intent(this, EditActivity.class);
        goEdit.putExtra("id", String.valueOf(showId.getText()));
        startActivity(goEdit);
        finish();
    }

    private void delete() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "DELETE FROM `transaction` WHERE id = ?;";
        db.execSQL(sql, new Object[] {showId.getText()});
        Toast.makeText(this, "Transaction Deleted Succesfully", Toast.LENGTH_LONG).show();
        finish();
    }

}