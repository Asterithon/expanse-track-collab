package com.example.expanse_track;

import java.text.NumberFormat;
import java.util.Locale;

public class Transaction {
    private int amount;
    private String description;
    private String date;

    private int type,id;

    // Constructor
    public Transaction(int amount, String description, String date, int type, int id) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.id = id;

    }

    // Getter untuk mengambil data
    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }



    @Override
    public String toString() {
        NumberFormat formatID = NumberFormat.getInstance(new Locale("id", "ID"));
        String formattedAmount = formatID.format(amount);

        return "Jumlah: " + formattedAmount + "\nDeskripsi: "
                + description + "\nTanggal: " + date + "\ntipe: " + type + "\nid: " + id;
    }

}
