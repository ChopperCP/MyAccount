package com.example.myaccount.models;


import com.example.myaccount.R;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    private final int imageIdIcon;
    private final int type;
    private String title;
    private String comment;
    private double amount;
    private Date date;

    public Transaction(int type, String title, String comment, double amount) {
        this.type = type;
        this.title = title;
        this.comment = comment;
        this.amount = amount;
        this.date = new Date();


        switch (type) {
            case Type.TYPE_INCOME:
                this.imageIdIcon = R.drawable.ic_income;
                break;
            case Type.TYPE_EXPENSE:
                this.imageIdIcon = R.drawable.ic_expense;
                break;
            default:
                this.imageIdIcon = R.drawable.ic_income;
                break;
        }
    }

    public int getImageIdIcon() {
        return imageIdIcon;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    // transaction types
    public static class Type {
        public static final int TYPE_INCOME = 1;
        public static final int TYPE_EXPENSE = TYPE_INCOME + 1;
    }
}
