package com.example.expanse_track;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {
    private ListView lvTransactions;
    private ArrayList<Transaction> list = new ArrayList<>();
    private ImageButton btnBack, btnInput, btnHome;
    private RadioGroup rdGroup;
    private RadioButton rdExpense, rdAll, rdIncome;
    // Ambil data dari intent
    private String currentFilter = "all";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lvTransactions = findViewById(R.id.list_transactions);


        btnBack = findViewById(R.id.btn_back);
        btnInput = findViewById(R.id.btn_input);
        rdGroup = findViewById(R.id.rd_group);
        rdIncome = findViewById(R.id.rd_income);
        rdExpense = findViewById(R.id.rd_expense);
        rdAll = findViewById(R.id.rd_all);
        btnHome = findViewById(R.id.btn_home);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboard();
            }
        });

        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rd_income) {
                    refresh("1"); // income
                } else if (checkedId == R.id.rd_expense) {
                    refresh("0"); // expense
                } else {
                    refresh("all"); // all
                }
            }
        });
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboard();
            }
        });

        lvTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemClick(i);
            }
        });
        lvTransactions.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemlongclick(i);
                return false;
            }
        });
    }
    private void itemlongclick(int i) {
        Transaction t = list.get(i);
        String[] menu = new String[] {"Edit", "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Action:");
        builder.setItems(menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which==0){
                    edit(t);
                }
                else if (which==1){
                    delete(t);
                }
            };
        });
        builder.create().show();
    }

    private void input() {
        Intent goInput = new Intent(this, InputActivity.class);
        startActivity(goInput);
    }

    private void itemClick(int i) {
        Transaction t = list.get(i);
        Intent goDetail = new Intent(this, DetailActivity.class);
        goDetail.putExtra("id", String.valueOf(t.getId()));
        startActivity(goDetail);
    }

    private void dashboard() {
        Intent goDashboard = new Intent(this, MainActivity.class);
        startActivity(goDashboard);
    }


    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String typeFilter = intent.getStringExtra("type");

        if (typeFilter == null) {
            typeFilter = "all";
        }

        refresh(typeFilter);
    }
    private void refresh(String typeFilter) {
        this.currentFilter = typeFilter;
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql;
        if (typeFilter.equals("1")) {
            sql = "SELECT amount, description, date, type, id FROM `transaction` WHERE type = 1 ORDER BY date DESC, id DESC";
        } else if (typeFilter.equals("0")) {
            sql = "SELECT amount, description, date, type, id FROM `transaction` WHERE type = 0 ORDER BY date DESC, id DESC";
        } else {
            sql = "SELECT amount, description, date, type, id FROM `transaction` ORDER BY date DESC, id DESC";
        }

        Cursor c = db.rawQuery(sql, null);

        list.clear();
        while (c.moveToNext()) {
            int amount = c.getInt(0);
            String description = c.getString(1);
            String date = c.getString(2);
            int type = c.getInt(3);
            int id = c.getInt(4);

            Transaction t = new Transaction(amount, description, date, type, id);
            list.add(t);
        }

        TransactionAdapter adapter = new TransactionAdapter(this, list);
        lvTransactions.setAdapter(adapter);
        // Set radio button sesuai filter yang sedang aktif
        if (typeFilter.equals("1")) {
            rdIncome.setChecked(true);
        } else if (typeFilter.equals("0")) {
            rdExpense.setChecked(true);
        } else {
            rdAll.setChecked(true);
        }
    }
    private void edit(Transaction t) {
        Intent goEdit = new Intent(this, EditActivity.class);
        goEdit.putExtra("id", String.valueOf(t.getId()));
        startActivity(goEdit);
    }

    private void delete(Transaction t) {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "DELETE FROM `transaction` WHERE `id` = ?;";
        db.execSQL(sql, new Object[] {t.getId()});
        Toast.makeText(this, "Transaction Deleted Successfully", Toast.LENGTH_LONG).show();
        refresh(currentFilter);
    }

}