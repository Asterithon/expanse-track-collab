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
    private ImageButton btnDashboard;


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
        showDate = findViewById(R.id.show_date);
        showDate.setVisibility(View.GONE);
        showType = findViewById(R.id.show_type);
        showType.setVisibility(View.GONE);
        inpAmount = findViewById(R.id.inp_amount);
        inpDesc = findViewById(R.id.inp_desc);
        tvType = findViewById(R.id.tv_type);
        ivIcon = findViewById(R.id.iv_icon);



        btnDashboard = findViewById(R.id.btn_dashboard);
        btnSubmit = findViewById(R.id.btn_submit);

        btnDashboard.setOnClickListener(new View.OnClickListener() {
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
        String stramount = "" + inpAmount.getText();
        String desc = "" + inpDesc.getText();
        String date = showDate.getText().toString();
        String tipe =  showType.getText().toString();
        String strid = showId.getText().toString();

        int id = Integer.parseInt(strid);
        int amount = Integer.parseInt(stramount);
        int type = Integer.parseInt(tipe);

        Transaction t = new Transaction(amount, desc, date, type, id);

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "UPDATE `transaction` SET `amount` = ?, `description` = ?, `date` = ?, `type` = ?  WHERE `id` = ?;";
        db.execSQL(sql, new Object[]{t.getAmount(), t.getDescription(), t.getDate(), t.getType(), t.getId()});
        Toast.makeText(this, "Transaction Edited Successfully", Toast.LENGTH_LONG).show();
        finish();

    }

}