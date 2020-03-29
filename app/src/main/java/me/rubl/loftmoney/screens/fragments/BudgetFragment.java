package me.rubl.loftmoney.screens.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.rubl.loftmoney.R;
import me.rubl.loftmoney.screens.activities.AddItemActivity;
import me.rubl.loftmoney.screens.adapters.ItemsAdapter;
import me.rubl.loftmoney.screens.model.ItemModel;

public class BudgetFragment extends Fragment {

    private ItemsAdapter mItemsAdapter = new ItemsAdapter();
    public static final int ADD_ITEM_REQUEST = 100;

    //TODO
    public ItemModel.BudgetType BUDGET_TYPE = ItemModel.BudgetType.INCOME;

    private RecyclerView mRecyclerMain;
    private FloatingActionButton mFabMain;

    private List mData;

    public static BudgetFragment newInstance(ItemModel.BudgetType budgetType) {
        BudgetFragment instance = new BudgetFragment();
        Bundle args = new Bundle();
        args.putSerializable("budget_type", budgetType);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BUDGET_TYPE = (ItemModel.BudgetType) getArguments().getSerializable("budget_type");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_budget, null);

        mRecyclerMain = rootView.findViewById(R.id.recyclerMain);
        mRecyclerMain.setAdapter(mItemsAdapter);

        mData = new ArrayList(Arrays.asList(
                new ItemModel("Item #4", "1", BUDGET_TYPE),
                new ItemModel("Item #3", "100", BUDGET_TYPE),
                new ItemModel("Item #2", "500", BUDGET_TYPE),
                new ItemModel("Item #1", "2000", BUDGET_TYPE)
        ));
        mItemsAdapter.setNewData(mData);

        mRecyclerMain.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false
        ));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL);
        mRecyclerMain.addItemDecoration(dividerItemDecoration);

        mFabMain = rootView.findViewById(R.id.fabMain);
        mFabMain.setOnClickListener((view) -> {
            startActivityForResult(new Intent(getActivity(), AddItemActivity.class).putExtra("budget_type", BUDGET_TYPE), ADD_ITEM_REQUEST);
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ITEM_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            ItemModel model = (ItemModel) data.getSerializableExtra(ItemModel.KEY_NAME);
            mItemsAdapter.addDataToTop(model);
        }
    }
}
