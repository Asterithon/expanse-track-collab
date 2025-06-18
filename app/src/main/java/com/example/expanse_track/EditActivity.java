package com.example.expanse_track;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditActivity extends AppCompatActivity {

    TextView showId, showAmount, showDesc, showDate, showType;

    private EditText inpAmount, inpDesc, inpDate, inpType;
    private Button btnDashboard, btnSubmit;


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
        showDesc = findViewById(R.id.show_desc);
        showAmount = findViewById(R.id.show_amount);
        showDate = findViewById(R.id.show_date);
        showType = findViewById(R.id.show_type);
        inpType = findViewById(R.id.inp_type);
        inpAmount = findViewById(R.id.inp_amount);
        inpDesc = findViewById(R.id.inp_desc);
        inpDate = findViewById(R.id.inp_date);


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

        inpType.setText(strtype);
        inpDate.setText(date);
        inpDesc.setText(description);
        showId.setText(strid);
        showId.setVisibility(View.GONE);
        showDesc.setText(String.format("Desc = %s", description));
        showAmount.setText(String.format("Amount = %s", stramount));
        showDate.setText(String.format("Date = %s", date));
        showType.setText(String.format("Type = %s", strtype));
    }}
    private void dashboard() {
        Intent goDashboard = new Intent(this, MainActivity.class);
        startActivity(goDashboard);
    }

    private void saveTransaction() {
        String stramount = "" + inpAmount.getText();
        String desc = "" + inpDesc.getText();
        String date = "" + inpDate.getText();
        String tipe = "" + inpType.getText();
        String strid = showId.getText().toString();

        int id = Integer.parseInt(strid);
        int amount = Integer.parseInt(stramount);
        int type = Integer.parseInt(tipe);

        Transaction t = new Transaction(amount, desc, date, type, id);

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "UPDATE `transaction` SET `amount` = ?, `description` = ?, `date` = ?, `type` = ?  WHERE `id` = ?;";
        db.execSQL(sql, new Object[]{t.getAmount(), t.getDescription(), t.getDate(), t.getType(), t.getId()});

        finish();

    }

}