package com.example.myaccount.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccount.R;
import com.example.myaccount.databinding.FragmentHomeBinding;
import com.example.myaccount.dataop.DataLoader;
import com.example.myaccount.models.Transaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private List<Transaction> transactions;
    private DataLoader dataLoader;
    private TransactionRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        initdata();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        this.adapter=new TransactionRecyclerViewAdapter(transactions);
        recyclerView.setAdapter(adapter);

//        Calculate the net asset, total income, and total expense
        TextView netAssetView=root.findViewById(R.id.net_asset_view);
        TextView totalIncomeView = root.findViewById(R.id.total_income_view);
        TextView totalExpenseView = root.findViewById(R.id.total_expense_view);

        Double totalIncome=0.0;
        Double totalExpense=0.0;
        Double netAsset=0.0;
        for (Transaction transaction:transactions){
            if (transaction.getType()==Transaction.Type.TYPE_INCOME){
                totalIncome+=transaction.getAmount();
            }
            else if (transaction.getType()==Transaction.Type.TYPE_EXPENSE){
                totalExpense+=transaction.getAmount();
            }
        }
        netAsset=totalIncome-totalExpense;

        totalIncomeView.setText(String.valueOf(totalIncome));
        totalExpenseView.setText(String.valueOf(totalExpense));
        netAssetView.setText(String.valueOf(netAsset));

        // Listen to the floating button
        FloatingActionButton fab = root.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogueView= LayoutInflater.from(getContext()).inflate(R.layout.dialogue_input_item,null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setView(dialogueView);

                alertDialogBuilder.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText typeInput=dialogueView.findViewById(R.id.type_input);
                        EditText titleInput=dialogueView.findViewById(R.id.title_input);
                        EditText commentInput=dialogueView.findViewById(R.id.comment_input);
                        EditText amountInput=dialogueView.findViewById(R.id.amount_input);

                        int type;
                        if (typeInput.getText().toString().equals("TYPE_INCOME")){
                            type=Transaction.Type.TYPE_INCOME;
                        }
                        else if (typeInput.getText().toString().equals("TYPE_EXPENSE")){
                            type=Transaction.Type.TYPE_EXPENSE;
                        }
                        else{
                            type=Transaction.Type.TYPE_INCOME;
                        }
                        transactions.add(new Transaction(type,titleInput.getText().toString(),commentInput.getText().toString(),Double.valueOf(amountInput.getText().toString())));
                        adapter.notifyItemInserted(transactions.size());

                        // Give user feedback
                        Toast.makeText(getContext(),"Transaction Added", Toast.LENGTH_LONG).show();
                        dataLoader.saveData();      // write to file
                    }
                });
                alertDialogBuilder.setCancelable(false).setNegativeButton ("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialogBuilder.create().show();
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        dataLoader.saveData();
    }

    public void initdata() {
        this.dataLoader = new DataLoader(getContext());
        this.transactions = dataLoader.loadData();
        if (transactions.size() == 0) {
            // fill some data if none is present.
            transactions.add(new Transaction(Transaction.Type.TYPE_INCOME, "Salary", "Nov Salary", 20000));
            transactions.add(new Transaction(Transaction.Type.TYPE_EXPENSE, "Rent", "Nov Rent", 5000));
        }
    }

}

class TransactionRecyclerViewAdapter extends RecyclerView.Adapter {
    private final List<Transaction> transactions;
    private TransactionHolder transactionViewHolder;

    public TransactionRecyclerViewAdapter(List<Transaction> transactions) {
        this.transactions = transactions;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder, parent, false);

        return new TransactionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        this.transactionViewHolder = (TransactionHolder) holder;

        transactionViewHolder.getImageView().setImageResource(transactions.get(position).getImageIdIcon());
        transactionViewHolder.getTitleView().setText(transactions.get(position).getTitle());
        transactionViewHolder.getCommentView().setText(transactions.get(position).getComment());
        transactionViewHolder.getAmountView().setText(String.valueOf(transactions.get(position).getAmount()));

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class TransactionHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private final ImageView imageView;
        private final TextView titleView;
        private final TextView commentView;
        private final TextView amountView;

        public View getItemView() {
            return itemView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getTitleView() {
            return titleView;
        }

        public TextView getCommentView() {
            return commentView;
        }

        public TextView getAmountView() {
            return amountView;
        }

        public TransactionHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.imageView = itemView.findViewById(R.id.imageView_icon);
            this.titleView = itemView.findViewById(R.id.textView_title);
            this.commentView = itemView.findViewById(R.id.textView_comment);
            this.amountView = itemView.findViewById(R.id.textView_amount);
        }
    }
}