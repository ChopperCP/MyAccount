package com.example.myaccount.ui.typeview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccount.R;
import com.example.myaccount.dataop.DataLoader;
import com.example.myaccount.models.Transaction;
import com.example.myaccount.ui.home.HomeRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class TypeViewExpenseFragment extends Fragment {
    private RecyclerView recyclerView;
    private HomeRecyclerViewAdapter adapter;
    private DataLoader dataLoader;
    private List<Transaction> transactions;
    private List<Transaction> incomes=new ArrayList<>();
    private List<Transaction> expenses=new ArrayList<>();
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_typesort_main,container);
        this.rootView=root;

        initData();
        this.recyclerView = root.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        this.adapter = new HomeRecyclerViewAdapter(expenses, dataLoader,adapter);
        recyclerView.setAdapter(adapter);


        return root;
    }

    public void initData() {
        this.dataLoader = new DataLoader(rootView.getContext());
        this.transactions = dataLoader.loadData();
        if (transactions.size() == 0) {
            // fill some data if none is present.
            transactions.add(new Transaction(Transaction.Type.TYPE_INCOME, "Salary", "Nov Salary", 20000));
            transactions.add(new Transaction(Transaction.Type.TYPE_EXPENSE, "Rent", "Nov Rent", 5000));
            Calendar aMonthAgo = Calendar.getInstance();
            aMonthAgo.add(Calendar.MONTH, -1);
            transactions.add(new Transaction(Transaction.Type.TYPE_INCOME, "Salary", "Oct Salary", 20000, aMonthAgo));

            dataLoader.saveData();
        }

        // sort the transaction by date in reverse order.
        Collections.sort(transactions,Collections.reverseOrder());
        for (Transaction transaction:transactions){
            switch (transaction.getType()){
                case Transaction.Type.TYPE_INCOME:
                    this.incomes.add(transaction);
                    break;
                case Transaction.Type.TYPE_EXPENSE:
                    this.expenses.add(transaction);
                    break;
            }
        }

    }
}
