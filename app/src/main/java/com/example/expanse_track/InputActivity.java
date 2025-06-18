package com.example.expanse_track;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InputActivity extends AppCompatActivity {

    private EditText inpAmount, inpDesc, inpDate, inpType;
    Button btnDashboard, btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        inpAmount = findViewById(R.id.inp_amount);
        inpDesc = findViewById(R.id.inp_desc);
        inpDate = findViewById(R.id.inp_date);
        inpType = findViewById(R.id.inp_type);

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
    }

    private void dashboard() {
        Intent goDashboard = new Intent(this, MainActivity.class);
        startActivity(goDashboard);
    }

    private void saveTransaction() {
        String stramount = "" + inpAmount.getText();
        String desc = "" + inpDesc.getText();
        String date = "" + inpDate.getText();
        String tipe = "" + inpType.getText();

        Integer id=0;
        int amount = Integer.parseInt(stramount);
        int type = Integer.parseInt(tipe);

        Transaction t = new Transaction(amount, desc, date, type, id);

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        String sql = "INSERT INTO `transaction`(`amount`, `description`, `date`, `type`) VALUES (?,?,?,?);";
        db.execSQL(sql, new Object[]{t.getAmount(), t.getDescription(), t.getDate(), t.getType()});

        finish();

    }

}