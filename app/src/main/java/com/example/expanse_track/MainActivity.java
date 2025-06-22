package com.example.expanse_track;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private ListView lvTransactions;
    private TextView tvTotal, tvRevenue, tvExpense;
    private ArrayList<Transaction> list = new ArrayList<>();
    Button btnShowIncome, btnShowExpense;
    ImageButton btnInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        tvTotal = findViewById(R.id.tv_total);
        tvRevenue = findViewById(R.id.tv_revenue);
        tvExpense = findViewById(R.id.tv_expense);
        btnInput = findViewById(R.id.btn_input);
        btnShowExpense = findViewById(R.id.btn_show_expense);
        btnShowIncome = findViewById(R.id.btn_show_income);
        lvTransactions = findViewById(R.id.list_transactions);

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

    btnInput.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            input();
        }
    });

    btnShowExpense.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showExpense();
        }
    });

    btnShowIncome.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showIncome();
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

    private void edit(Transaction t) {
        Intent goEdit = new Intent(this, EditActivity.class);
        goEdit.putExtra("id", String.valueOf(t.getId()));
        startActivity(goEdit);
        refresh();
    }

    private void delete(Transaction t) {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "DELETE FROM `transaction` WHERE `id` = ?;";
        db.execSQL(sql, new Object[] {t.getId()});
        Toast.makeText(this, "Transaction Deleted Succesfully", Toast.LENGTH_LONG).show();
        refresh();
    }

    private void itemClick(int i) {
        Transaction t = list.get(i);
        Intent goDetail = new Intent(this, DetailActivity.class);
        goDetail.putExtra("id", String.valueOf(t.getId()));
        startActivity(goDetail);
//        Toast.makeText(this, t.getDescription(), Toast.LENGTH_LONG).show();
        refresh();
    }

    private void showIncome() {
        String type = "1";
        Intent goTransaction = new Intent(this, TransactionActivity.class);
        goTransaction.putExtra("type", type);
        startActivity(goTransaction);
    }

    private void showExpense() {
        String type = "0";
        Intent goTransaction = new Intent(this, TransactionActivity.class);
        goTransaction.putExtra("type", type);
        startActivity(goTransaction);
    }

    private void input() {
        Intent goInput = new Intent(this, InputActivity.class);
        startActivity(goInput);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT `id`, `amount`, `description`, `date`, `type` FROM `transaction` ORDER BY date DESC, id DESC";
        Cursor c = db.rawQuery(sql, new String[0]);

        //ambil data dari database
        list.clear();
        while(c.moveToNext()) {
            int id = c.getInt(0);
            int amount = c.getInt(1);
            String description = c.getString(2);
            String date = c.getString(3);
            int type = c.getInt(4);

            Transaction t = new Transaction(amount, description, date, type, id);
            list.add(t);
        }

        //update ke list bang
        TransactionAdapter adapter = new TransactionAdapter(this, list);
        lvTransactions.setAdapter(adapter);


        String sqlRevenue = "SELECT SUM(`amount`) AS total_revenue FROM `transaction` WHERE `type` = 1;";
        String sqlExpense = "SELECT SUM(`amount`) AS total_expense FROM `transaction` WHERE `type` = 0;";
        Cursor totalRev = db.rawQuery(sqlRevenue, null);
        Cursor totalExp = db.rawQuery(sqlExpense, null);


        double totalRevenue = 0;
        if (totalRev.moveToFirst()) {
            totalRevenue = totalRev.getDouble(0);
        }
        totalRev.close();

        double totalExpense = 0;
        if (totalExp.moveToFirst()) {
            totalExpense = totalExp.getDouble(0);
        }
        totalExp.close();

        // Update textview set total to format IDR
        Locale localeID = new Locale("in", "ID");

        NumberFormat formatNumber = NumberFormat.getNumberInstance(localeID);
        String formattedRevenue = formatNumber.format(totalRevenue);
        String formattedExpense = formatNumber.format(totalExpense);
        String formattedBalance = formatNumber.format(totalRevenue - totalExpense);

        tvRevenue.setText(formattedRevenue);
        tvExpense.setText(formattedExpense);
        tvTotal.setText("Rp " + formattedBalance);

    }


}