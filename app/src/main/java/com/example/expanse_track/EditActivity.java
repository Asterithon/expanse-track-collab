package com.example.expanse_track;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditActivity extends AppCompatActivity {

    private TextView showId, showDate, showType, tvType;
    private ImageView ivIcon;

    private EditText inpAmount, inpDesc;
    private Button btnSubmit;
    private ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showId = findViewById(R.id.show_id);
        showId.setVisibility(View.GONE);
        showDate = findViewById(R.id.tv_date);
        showType = findViewById(R.id.show_type);
        showType.setVisibility(View.GONE);
        inpAmount = findViewById(R.id.inp_amount);
        inpDesc = findViewById(R.id.inp_desc);
        tvType = findViewById(R.id.tv_type);
        ivIcon = findViewById(R.id.iv_icon);



        btnBack = findViewById(R.id.btn_back);
        btnSubmit = findViewById(R.id.btn_submit);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboard();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTransaction();
            }
        });

        Intent intentEdit = getIntent();

        String strid = intentEdit.getStringExtra("id");
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String dt = ("SELECT `amount`, `description`, `date`, `type` FROM `transaction` WHERE id ='" + strid + "'");
        Cursor data = db.rawQuery(dt, new String[0]);

        if (data.moveToFirst()) {
            String stramount = data.getString(0);
            String description = data.getString(1);
            String date = data.getString(2);
            String strtype = data.getString(3);

            try {
                int angka = Integer.parseInt(stramount);
                inpAmount.setText(String.valueOf(angka));
            } catch (NumberFormatException e) {
                inpAmount.setText("0"); // Default jika terjadi kesalahan konversi
            }

            inpDesc.setText(description);
            showId.setText(strid);
            showDate.setText(date);
            showType.setText(strtype);

            int type = Integer.parseInt(strtype);
            ivIcon.setImageResource(type == 0 ? R.drawable.baseline_arrow_downward_24 : R.drawable.baseline_arrow_upward_24);
            tvType.setText(type == 0 ? "Expense" : "Income");
        }}
    private void dashboard() {
//        Intent goDashboard = new Intent(this, MainActivity.class);
//        startActivity(goDashboard);
        finish();
    }

    private void saveTransaction() {
        String stramount = inpAmount.getText().toString().trim();
        String desc = inpDesc.getText().toString().trim();
        String date = showDate.getText().toString().trim();
        String tipe = showType.getText().toString().trim();
        String strid = showId.getText().toString().trim();

        if (stramount.isEmpty() || desc.isEmpty() || date.isEmpty() || tipe.isEmpty() || strid.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields before saving", Toast.LENGTH_SHORT).show();
            return;
        }

        int id, amount, type;
        try {
            id = Integer.parseInt(strid);
            amount = Integer.parseInt(stramount);
            type = Integer.parseInt(tipe);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            return;
        }

        Transaction t = new Transaction(amount, desc, date, type, id);

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "UPDATE `transaction` SET `amount` = ?, `description` = ?, `date` = ?, `type` = ? WHERE `id` = ?;";
        db.execSQL(sql, new Object[]{t.getAmount(), t.getDescription(), t.getDate(), t.getType(), t.getId()});

        Toast.makeText(this, "Transaction Edited Successfully", Toast.LENGTH_LONG).show();
        finish();
    }

}