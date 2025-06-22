package com.example.expanse_track;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InputActivity extends AppCompatActivity {

    private TextView tvSelectedDate;
    private EditText inpAmount, inpDesc;
    private RadioGroup rdGroup;
    private RadioButton rdExpense, rdRevenue;
    Button btnSubmit, btnSelectDate;
    ImageButton btnDashboard;

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

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String todayDate = sdf.format(calendar.getTime());
        btnSelectDate = findViewById(R.id.btnSelectDate);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvSelectedDate.setText(todayDate);
        btnSelectDate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        @SuppressLint("DefaultLocale") String selectedDate = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear);
                        tvSelectedDate.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        inpAmount = findViewById(R.id.inp_amount);
        inpDesc = findViewById(R.id.inp_desc);
        btnDashboard = findViewById(R.id.btn_dashboard);
        btnSubmit = findViewById(R.id.btn_submit);
        rdGroup = findViewById(R.id.rd_group);
        rdExpense = findViewById(R.id.rd_expense);
        rdRevenue = findViewById(R.id.rd_revenue);

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
        String date = "" + tvSelectedDate.getText();

        Integer id=0;
        int amount = Integer.parseInt(stramount);

        int selectedId = rdGroup.getCheckedRadioButtonId();
        int type = -1; // default

        if (selectedId == R.id.rd_expense) {
            type = 0;  // Pengeluaran = 0
            rdExpense.setTextColor(Color.WHITE);
        } else if (selectedId == R.id.rd_revenue) {
            type = 1;  // Pemasukan = 1
            rdRevenue.setTextColor(Color.WHITE);
        }

        Transaction t = new Transaction(amount, desc, date, type, id);

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        String sql = "INSERT INTO `transaction`(`amount`, `description`, `date`, `type`) VALUES (?,?,?,?);";
        db.execSQL(sql, new Object[]{t.getAmount(), t.getDescription(), t.getDate(), t.getType()});
        Toast.makeText(this, "Transaction Recorded Successfully", Toast.LENGTH_LONG).show();
        finish();

    }

}