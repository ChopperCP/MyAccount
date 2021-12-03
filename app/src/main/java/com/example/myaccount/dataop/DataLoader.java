package com.example.myaccount.dataop;

import android.content.Context;

import com.example.myaccount.models.Transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.myaccount.R;

public class DataLoader {
    private final String DATA_FILE_NAME;
    private Context context = null;
    List<Transaction> transactions;

    public DataLoader(Context context) {
        this.context = context;
        this.DATA_FILE_NAME = context.getResources().getString(R.string.DATA_FILE_NAME);
    }

    public List<Transaction> loadData() {
        transactions = new ArrayList<>();
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(context.openFileInput(DATA_FILE_NAME));
            transactions = (ArrayList<Transaction>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
                if (transactions==null || transactions.size()==0){
                    transactions=new ArrayList<>();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return transactions;
    }

    public void saveData() {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(context.openFileOutput(DATA_FILE_NAME, Context.MODE_PRIVATE));
            objectOutputStream.writeObject(transactions);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
