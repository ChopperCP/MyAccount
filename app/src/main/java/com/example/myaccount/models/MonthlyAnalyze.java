package com.example.myaccount.models;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;

public class MonthlyAnalyze {
    private YearMonth yearMonth;
    private double totalIncome;
    private double totalExpense;

    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getTotalExpense() {
        return totalExpense;
    }


    public MonthlyAnalyze(YearMonth yearMonth, List<Transaction> transactions) {
        this.yearMonth = yearMonth;
        this.totalIncome = 0.0;
        this.totalExpense = 0.0;

        for (Transaction transaction : transactions) {
            switch (transaction.getType()) {
                case Transaction.Type.TYPE_INCOME:
                    totalIncome += transaction.getAmount();
                    break;
                case Transaction.Type.TYPE_EXPENSE:
                    totalExpense += transaction.getAmount();
                    break;
            }
        }

    }
}
