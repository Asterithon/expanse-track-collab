package com.example.expanse_track;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TransactionAdapter extends ArrayAdapter<Transaction> {
    private TextView tvDate, tvAmount, tvDescription, tvType;
    private ImageView ivIcon;
    public TransactionAdapter(Context context, ArrayList<Transaction> transaction) {
        super(context, 0, transaction);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transaction transaction = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_transaction, parent, false);
        }

        LinearLayout layoutItem = convertView.findViewById(R.id.layout_item);
         tvDate = convertView.findViewById(R.id.tv_date);
         tvAmount = convertView.findViewById(R.id.tv_amount);
         tvDescription = convertView.findViewById(R.id.tv_description);
         tvType = convertView.findViewById(R.id.tv_type);
         ivIcon = convertView.findViewById(R.id.iv_icon);


        Locale localeID = new Locale("in", "ID");
        NumberFormat formatNumber = NumberFormat.getNumberInstance(localeID);
        String formattedAmount = formatNumber.format(transaction.getAmount());

        tvDate.setText(transaction.getDate());
        tvAmount.setText(transaction.getType() == 0 ? "IDR -" + formattedAmount : "IDR +" + formattedAmount);
        tvAmount.setTextColor(transaction.getType() == 0 ? Color.parseColor("#DAFB1F68") : Color.parseColor("#61CE65"));
        tvDescription.setText(transaction.getDescription());
        tvType.setText(transaction.getType() == 0 ? "Expense" : "Income");

        ivIcon.setImageResource(transaction.getType() == 0 ? R.drawable.baseline_arrow_downward_24 : R.drawable.baseline_arrow_upward_24);
        return convertView;
    }
}