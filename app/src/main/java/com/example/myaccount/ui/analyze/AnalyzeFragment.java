package com.example.myaccount.ui.analyze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccount.R;
import com.example.myaccount.databinding.FragmentAnalyzeBinding;
import com.example.myaccount.dataop.DataLoader;
import com.example.myaccount.models.MonthlyAnalyze;
import com.example.myaccount.models.Transaction;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;

public class AnalyzeFragment extends Fragment {

    private AnalyzeViewModel analyzeViewModel;
    private FragmentAnalyzeBinding binding;
    private DataLoader dataLoader;
    private View rootView;
    private List<Transaction> transactions;
    private AnalyzeRecyclerViewAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        analyzeViewModel =
                new ViewModelProvider(this).get(AnalyzeViewModel.class);

        binding = FragmentAnalyzeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.rootView = root;

        initdata();

        // group the transactions into different year-months (Yes, we don't want the same month of different years got grouped together)
        SortedMap<YearMonth,List<Transaction>> yearMonthToMonthlyTransactions= new TreeMap<>();
        for (Transaction transaction : transactions) {
            Calendar date = transaction.getDate();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = localDate.getYear();
            int month = localDate.getMonthValue();
            YearMonth yearMonth=YearMonth.of(year,month);

            List<Transaction> monthlyTransactions = new ArrayList<>();
            if (yearMonthToMonthlyTransactions.containsKey(yearMonth)){
                monthlyTransactions = yearMonthToMonthlyTransactions.get(yearMonth);
            }
            monthlyTransactions.add(transaction);
            yearMonthToMonthlyTransactions.put(yearMonth,monthlyTransactions);
        }
        // get monthly analyzes in descending order
        List<MonthlyAnalyze> monthlyAnalyzes=new ArrayList<>();
        for (Map.Entry<YearMonth,List<Transaction>> sortedEntries: yearMonthToMonthlyTransactions.entrySet()){
            MonthlyAnalyze monthlyAnalyze=new MonthlyAnalyze(sortedEntries.getKey(),sortedEntries.getValue());
            monthlyAnalyzes.add(0,monthlyAnalyze);
        }


        // set up recycler view's adapter
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        this.adapter = new AnalyzeRecyclerViewAdapter(monthlyAnalyzes);
        recyclerView.setAdapter(adapter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void initdata() {
        this.dataLoader = new DataLoader(rootView.getContext());
        this.transactions = dataLoader.loadData();
    }
}

class AnalyzeRecyclerViewAdapter extends RecyclerView.Adapter {
    private final List<MonthlyAnalyze> monthlyAnalyzes;
    private AnalyzeHolder transactionViewHolder;

    public AnalyzeRecyclerViewAdapter(List<MonthlyAnalyze> monthlyAnalyzes) {
        this.monthlyAnalyzes = monthlyAnalyzes;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.analyze_holder, parent, false);

        return new AnalyzeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        this.transactionViewHolder = (AnalyzeHolder) holder;

        transactionViewHolder.getDateView().setText(monthlyAnalyzes.get(position).getYearMonth().toString());
        transactionViewHolder.getTotalIncomeView().setText(String.valueOf(monthlyAnalyzes.get(position).getTotalIncome()));
        transactionViewHolder.getTotalExpenseView().setText(String.valueOf(monthlyAnalyzes.get(position).getTotalExpense()));
    }

    @Override
    public int getItemCount() {
        return monthlyAnalyzes.size();
    }

    class AnalyzeHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private final TextView dateView;
        private final TextView totalIncomeView;
        private final TextView totalExpenseView;

        public TextView getDateView() {
            return dateView;
        }

        public TextView getTotalIncomeView() {
            return totalIncomeView;
        }

        public TextView getTotalExpenseView() {
            return totalExpenseView;
        }

        public AnalyzeHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.dateView=itemView.findViewById(R.id.date_view);
            this.totalIncomeView=itemView.findViewById(R.id.total_income_view);
            this.totalExpenseView=itemView.findViewById(R.id.total_expense_view);
        }
    }
}