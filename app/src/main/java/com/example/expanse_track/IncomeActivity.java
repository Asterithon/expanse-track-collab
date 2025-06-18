package com.example.expanse_track;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class IncomeActivity extends AppCompatActivity {

    private ListView lvTransactions;
    private ArrayList<Transaction> list = new ArrayList<>();
    private Button btnDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_income);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lvTransactions = findViewById(R.id.list_transactions);

        btnDashboard = findViewById(R.id.btn_dashboard);
        btnDashboard.setOnClickListener(new View.OnClickListener() {
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
        String[] menu = new String[] {"edit", "hapus"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ini title");
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
        refresh();
    }

    private void itemClick(int i) {
        Transaction t = list.get(i);
        Intent goDetail = new Intent(this, DetailActivity.class);
        goDetail.putExtra("id", String.valueOf(t.getId()));
        startActivity(goDetail);
        Toast.makeText(this, t.getDescription(), Toast.LENGTH_LONG).show();
        refresh();
    }

    private void dashboard() {
        Intent goDashboard = new Intent(this, MainActivity.class);
        startActivity(goDashboard);
    }


    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
    private void refresh() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT `amount`, `description`, `date`, `type`, `id` FROM `transaction` WHERE 1 & `type` = 1";
        Cursor c = db.rawQuery(sql, new String[0]);

        //ambil data dari dtbsase
        list.clear();
        while(c.moveToNext()) {
            int amount = c.getInt(0);
            String description = c.getString(1);
            String date = c.getString(2);
            int type = c.getInt(3);
            int id = c.getInt(4);

            Transaction t = new Transaction(amount, description, date, type, id);
            list.add(t);
        }

        //update ke list bang
        lvTransactions.setAdapter(new ArrayAdapter<Transaction>(this,
                android.R.layout.simple_list_item_1, list));
    }

}