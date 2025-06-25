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

import java.text.NumberFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    TextView showId, showAmount, showDesc, showDate, tvType;
    ImageButton btnBack, btnHome, btnInput, btnShowTransaction;
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
        btnBack = findViewById(R.id.btn_back);
        btnInput =findViewById(R.id.btn_input);
        btnShowTransaction = findViewById(R.id.btn_show_transaction);
        btnHome = findViewById(R.id.btn_home);
        btnDlt = findViewById(R.id.btn_dlt);
        btnEdt = findViewById(R.id.btn_edt);

        ivIcon = findViewById(R.id.iv_icon);
        tvType = findViewById(R.id.tv_type);

        btnShowTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransaction();
            }
        });
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input();
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home();
            }
        });
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
        btnBack.setOnClickListener(new View.OnClickListener() {
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
            int stramount = data.getInt(0);
            String description = data.getString(1);
            String date = data.getString(2);
            String strtype = data.getString(3);


            Locale localeID = new Locale("in", "ID");
            NumberFormat formatNumber = NumberFormat.getNumberInstance(localeID);
            String formattedAmount = formatNumber.format(stramount);

            // Masukkan data ke dalam EditText atau TextView
            showId.setText(strid);
            showDate.setText(date);
            showAmount.setText(String.format("IDR %s", formattedAmount));
            showDesc.setText(description);


            int type = Integer.parseInt(strtype);
            ivIcon.setImageResource(type == 0 ? R.drawable.baseline_arrow_downward_24 : R.drawable.baseline_arrow_upward_24);
            tvType.setText(type == 0 ? "Expense" : "Income");

        }
    }

    private void showTransaction() {
        String type = "all";
        Intent goTransaction = new Intent(this, TransactionActivity.class);
        goTransaction.putExtra("type", type);
        startActivity(goTransaction);
    }

    private void input() {
        Intent goInput = new Intent(this, InputActivity.class);
        startActivity(goInput);
    }

    private void home() {
        Intent goDashboard = new Intent(this, MainActivity.class);
        startActivity(goDashboard);
    }

    private void dashboard() {
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