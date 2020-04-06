package me.rubl.loftmoney.screens.screens.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.rubl.loftmoney.R;
import me.rubl.loftmoney.screens.screens.main.adapter.ItemsAdapter;
import me.rubl.loftmoney.screens.screens.main.model.ItemModel;
import me.rubl.loftmoney.screens.web.WebFactory;
import me.rubl.loftmoney.screens.web.model.AuthResponse;
import me.rubl.loftmoney.screens.web.model.ItemRemote;

public class BudgetFragment extends Fragment {

    public static final String KEY_BUDGET_TYPE = "budget_type";

    private ItemModel.BudgetType mBudgetType;
    private List<Disposable> mDisposables = new ArrayList<>();
    private ItemsAdapter mItemsAdapter = new ItemsAdapter();

    private SwipeRefreshLayout mSwipeRefreshLayoutMain;
    private RecyclerView mRecyclerMain;

    public static BudgetFragment newInstance(ItemModel.BudgetType budgetType) {
        BudgetFragment instance = new BudgetFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_BUDGET_TYPE, budgetType);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mBudgetType = (ItemModel.BudgetType) getArguments().getSerializable(KEY_BUDGET_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_budget, null);

        mRecyclerMain = rootView.findViewById(R.id.recycler_main);
        mRecyclerMain.setAdapter(mItemsAdapter);

        mRecyclerMain.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false
        ));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL);
        mRecyclerMain.addItemDecoration(dividerItemDecoration);

        mSwipeRefreshLayoutMain = rootView.findViewById(R.id.swipe_refresh_layout_main);
        mSwipeRefreshLayoutMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        return rootView;
    }

    public ItemModel.BudgetType getBudgetType() {
        return mBudgetType;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadItems();
    }

    @Override
    public void onStop() {
        super.onStop();

        for(Disposable disposable : mDisposables) {
            disposable.dispose();
        }
    }

    private void loadItems() {

        SharedPreferences preferences = getActivity().getSharedPreferences(getActivity().getString(R.string.app_name), Context.MODE_PRIVATE);
        String authToken = preferences.getString(AuthResponse.AUTH_TOKEN_KEY, "");

        Disposable getItemsRequest = WebFactory.getInstance().getApi().getItems(mBudgetType.getStringType(), authToken)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ItemRemote>>() {
                    @Override
                    public void accept(List<ItemRemote> itemRemotes) throws Exception {

                        mSwipeRefreshLayoutMain.setRefreshing(false);

                        List<ItemModel> loadedItems = new ArrayList<>();

                        for (ItemRemote itemRemote : itemRemotes) {
                            loadedItems.add(new ItemModel(itemRemote));
                        }

                        sortItemsById(loadedItems);

                        mItemsAdapter.setNewData(loadedItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        mSwipeRefreshLayoutMain.setRefreshing(false);

                        Toast.makeText(getActivity(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        mDisposables.add(getItemsRequest);
    }

    private void sortItemsById(List<ItemModel> items) {
        Collections.sort(items, new Comparator<ItemModel>() {
            @Override
            public int compare(ItemModel o1, ItemModel o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
    }

}
