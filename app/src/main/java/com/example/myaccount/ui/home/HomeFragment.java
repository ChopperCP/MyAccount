package com.example.myaccount.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccount.R;
import com.example.myaccount.databinding.FragmentHomeBinding;
import com.example.myaccount.dataop.DataLoader;
import com.example.myaccount.models.Transaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private View rootView;

    private List<Transaction> transactions;
    private DataLoader dataLoader;
    private HomeRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.rootView=root;


        initdata();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        this.adapter = new HomeRecyclerViewAdapter(transactions);
        recyclerView.setAdapter(adapter);

//        Calculate the net asset, total income, and total expense
        TextView netAssetView = root.findViewById(R.id.net_asset_view);
        TextView totalIncomeView = root.findViewById(R.id.total_income_view);
        TextView totalExpenseView = root.findViewById(R.id.total_expense_view);

        Double totalIncome = 0.0;
        Double totalExpense = 0.0;
        Double netAsset = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getType() == Transaction.Type.TYPE_INCOME) {
                totalIncome += transaction.getAmount();
            } else if (transaction.getType() == Transaction.Type.TYPE_EXPENSE) {
                totalExpense += transaction.getAmount();
            }
        }
        netAsset = totalIncome - totalExpense;

        totalIncomeView.setText(String.valueOf(totalIncome));
        totalExpenseView.setText(String.valueOf(totalExpense));
        netAssetView.setText(String.valueOf(netAsset));

        // Listen to the floating button
        FloatingActionButton fab = root.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View inputDialogueView = LayoutInflater.from(getContext()).inflate(R.layout.dialogue_input_item, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setView(inputDialogueView);

                Button typeInput = inputDialogueView.findViewById(R.id.type_input);
                EditText titleInput = inputDialogueView.findViewById(R.id.title_input);
                EditText commentInput = inputDialogueView.findViewById(R.id.comment_input);
                EditText amountInput = inputDialogueView.findViewById(R.id.amount_input);

                final int[] type = new int[] {Transaction.Type.TYPE_INCOME};

                // menu to select transaction type
                typeInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                                View selectTransactionTypeDialogueView = LayoutInflater.from(getContext()).inflate(R.layout.dialogue_select_transaction_type,null);
                        AlertDialog.Builder selectTransactionTypeDialogueBuilder = new AlertDialog.Builder(view.getContext());
                        selectTransactionTypeDialogueBuilder.setTitle("选择类型");
//                                selectTransactionTypeDialogueBuilder.setView(selectTransactionTypeDialogueView);
                        String[] availableTypes = new String[]{
                                "Income",
                                "Expense"
                        };
                        selectTransactionTypeDialogueBuilder.setItems(availableTypes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                typeInput.setText(availableTypes[which]);
                            }
                        });

                        selectTransactionTypeDialogueBuilder.show();

                    }
                });

                if (typeInput.getText().toString().equals("Income")) {
                    type[0] = Transaction.Type.TYPE_INCOME;
                } else if (typeInput.getText().toString().equals("Expense")) {
                    type[0] = Transaction.Type.TYPE_EXPENSE;
                }

                alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        transactions.add(new Transaction(type[0], titleInput.getText().toString(), commentInput.getText().toString(), Double.valueOf(amountInput.getText().toString())));
                        adapter.notifyItemInserted(transactions.size());

                        // Give user feedback
                        Toast.makeText(getContext(), "Transaction Added", Toast.LENGTH_LONG).show();
                        dataLoader.saveData();      // write to file
                    }
                });
                alertDialogBuilder.setCancelable(false).setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
        this.dataLoader = new DataLoader(rootView.getContext());
        this.transactions = dataLoader.loadData();
        if (transactions.size() == 0) {
            // fill some data if none is present.
            transactions.add(new Transaction(Transaction.Type.TYPE_INCOME, "Salary", "Nov Salary", 20000));
            transactions.add(new Transaction(Transaction.Type.TYPE_EXPENSE, "Rent", "Nov Rent", 5000));
            Calendar aMonthAgo = Calendar.getInstance();
            aMonthAgo.add(Calendar.MONTH,-1);
            transactions.add(new Transaction(Transaction.Type.TYPE_INCOME, "Salary", "Oct Salary", 20000, aMonthAgo));
        }
    }

}

class HomeRecyclerViewAdapter extends RecyclerView.Adapter {
    private final List<Transaction> transactions;
    private TransactionHolder transactionViewHolder;

    public HomeRecyclerViewAdapter(List<Transaction> transactions) {
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
        transactionViewHolder.getDateView().setText(transactions.get(position).getDate().getTime().toString());
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
        private final TextView dateView;

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

        public TextView getDateView() {
            return dateView;
        }

        public TransactionHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.imageView = itemView.findViewById(R.id.imageView_icon);
            this.titleView = itemView.findViewById(R.id.textView_title);
            this.commentView = itemView.findViewById(R.id.textView_comment);
            this.amountView = itemView.findViewById(R.id.textView_amount);
            this.dateView=itemView.findViewById(R.id.textView_date);
        }
    }
}