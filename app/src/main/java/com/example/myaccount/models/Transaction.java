package com.example.myaccount.models;


import com.example.myaccount.R;

import java.io.Serializable;
import java.util.Calendar;

public class Transaction implements Serializable,Comparable {
    private final int imageIdIcon;
    private final int type;
    private String title;
    private String comment;
    private double amount;
    private Calendar date;

    public Transaction(int type, String title, String comment, double amount) {
        this.type = type;
        this.title = title;
        this.comment = comment;
        this.amount = amount;
        this.date = Calendar.getInstance();


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



    public Transaction(int type, String title, String comment, double amount, Calendar date) {
        this.type = type;
        this.title = title;
        this.comment = comment;
        this.amount = amount;
        this.date=date;


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

    public Calendar getDate() {
        return date;
    }

    @Override
    public int compareTo(Object o) {
        Transaction anotherTransaction=(Transaction) o;
        return this.getDate().compareTo(anotherTransaction.getDate());
    }

    // transaction types
    public static class Type {
        public static final int TYPE_INCOME = 1;
        public static final int TYPE_EXPENSE = TYPE_INCOME + 1;

        public static String getTypeIncomeText(){
            return "Income";
        }
        public static String getTypeExpenseText(){
            return "Expense";
        }
    }
}
