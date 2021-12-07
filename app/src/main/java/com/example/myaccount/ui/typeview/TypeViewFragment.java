package com.example.myaccount.ui.typeview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myaccount.R;
import com.example.myaccount.databinding.FragmentSortviewBinding;
import com.example.myaccount.models.Transaction;
import com.example.tab.TypeViewIncomeFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypeViewFragment extends Fragment {

    private TypeViewModel sortViewViewModel;
    private FragmentSortviewBinding binding;
    private View rootView;
    private TabLayout tab;
    private ViewPager2 viewPager;
    private List<Fragment> fragments = new ArrayList<>();//ViewPage2的Fragment容器
    private List<String> tabTitles = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sortViewViewModel =
                new ViewModelProvider(this).get(TypeViewModel.class);

        binding = FragmentSortviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.rootView=root;

        //找到控件
        tab = root.findViewById(R.id.tab_layout);
        viewPager = root.findViewById(R.id.viewpager2);

        initFragments();
        //创建适配器
        SortViewTabAdapter adapter = new SortViewTabAdapter(this.getActivity(), fragments);
        viewPager.setAdapter(adapter);

        //TabLayout与ViewPage2联动关键代码
        new TabLayoutMediator(tab, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabTitles.get(position));
            }
        }).attach();

        //ViewPage2选中改变监听
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        //TabLayout的选中改变监听
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initFragments() {
        tabTitles.add(Transaction.Type.getTypeIncomeText());
        tabTitles.add(Transaction.Type.getTypeExpenseText());

        TypeViewIncomeFragment incomeFragment = new TypeViewIncomeFragment();
        TypeViewExpenseFragment expenseFragment = new TypeViewExpenseFragment();

        fragments.add(incomeFragment);
        fragments.add(expenseFragment);
    }
}

class SortViewTabAdapter extends FragmentStateAdapter {
    List<Fragment> fragments = new ArrayList<>();

    public SortViewTabAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragments) {
        super(fragmentActivity);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}