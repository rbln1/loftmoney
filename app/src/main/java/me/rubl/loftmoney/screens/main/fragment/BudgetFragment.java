package me.rubl.loftmoney.screens.main.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.List;

import me.rubl.loftmoney.R;
import me.rubl.loftmoney.screens.main.presenters.MoneyPresenter;
import me.rubl.loftmoney.screens.main.view_state.MoneyViewState;
import me.rubl.loftmoney.screens.main.adapter.BudgetItemsAdapter;
import me.rubl.loftmoney.screens.main.interfaces.BudgetItemsAdapterListener;
import me.rubl.loftmoney.screens.main.model.BudgetItemModel;
import me.rubl.loftmoney.common.BudgetType;
import me.rubl.loftmoney.common.web.model.AuthResponseModel;

public class BudgetFragment extends Fragment implements MoneyViewState, BudgetItemsAdapterListener, ActionMode.Callback {

    public static final String KEY_BUDGET_TYPE = "budget_type";

    private BudgetType mBudgetType;
    private String authToken;
    private MoneyPresenter moneyPresenter = new MoneyPresenter();
    private BudgetItemsAdapter mBudgetItemsAdapter = new BudgetItemsAdapter();
    private ActionMode mActionMode;

    private SwipeRefreshLayout mSwipeRefreshLayoutMain;
    private RecyclerView mRecyclerMain;
    private CircularProgressView mLoadingCpv;
    private View mNoDataView;
    private View mDataLoadingErrorView;

    public static BudgetFragment getInstance(BudgetType budgetType) {
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
            mBudgetType = (BudgetType) getArguments().getSerializable(KEY_BUDGET_TYPE);
        }

        moneyPresenter.setMoneyViewState(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayoutMain = view.findViewById(R.id.fragment_budget_swipe_refresh_layout);
        mRecyclerMain = view.findViewById(R.id.fragment_budget_recycler_items);
        mLoadingCpv = view.findViewById(R.id.fragment_budget_cpv_loading);
        mNoDataView = view.findViewById(R.id.fragment_budget_ll_no_data);
        mDataLoadingErrorView = view.findViewById(R.id.fragment_budget_ll_loading_error);

        mBudgetItemsAdapter.setListener(this);
        mRecyclerMain.setAdapter(mBudgetItemsAdapter);

        mRecyclerMain.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false
        ));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        mRecyclerMain.addItemDecoration(dividerItemDecoration);

        mSwipeRefreshLayoutMain.setOnRefreshListener(() -> {
            if (mActionMode != null) {
                mActionMode.finish();
                mActionMode = null;
            }
            moneyPresenter.loadItems(mBudgetType.getStringType(), authToken);
        });

    }

    public BudgetType getBudgetType() {
        return mBudgetType;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() == null) return;

        SharedPreferences preferences = getActivity().getSharedPreferences(getActivity().getString(R.string.app_name), Context.MODE_PRIVATE);
        authToken = preferences.getString(AuthResponseModel.AUTH_TOKEN_KEY, "");

        if (!TextUtils.isEmpty(authToken))
            moneyPresenter.loadItems(mBudgetType.getStringType(), authToken);
    }

    @Override
    public void onStop() {
        super.onStop();

        moneyPresenter.onDestroy();
    }

    // MainViewState implementation

    @Override
    public void startLoading() {
        mBudgetItemsAdapter.clearSelections();
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
        mLoadingCpv.setVisibility(View.VISIBLE);
        mRecyclerMain.setVisibility(View.GONE);
        mNoDataView.setVisibility(View.GONE);
        mDataLoadingErrorView.setVisibility(View.GONE);
    }

    @Override
    public void showData(List<BudgetItemModel> data) {
        mSwipeRefreshLayoutMain.setRefreshing(false);
        mLoadingCpv.setVisibility(View.GONE);
        mRecyclerMain.setVisibility(View.VISIBLE);
        mNoDataView.setVisibility(View.GONE);
        mDataLoadingErrorView.setVisibility(View.GONE);

        mBudgetItemsAdapter.setNewData(data);
    }

    @Override
    public void showError(String error) {
        mSwipeRefreshLayoutMain.setRefreshing(false);
        mLoadingCpv.setVisibility(View.GONE);
        mRecyclerMain.setVisibility(View.GONE);
        mNoDataView.setVisibility(View.GONE);
        mDataLoadingErrorView.setVisibility(View.VISIBLE);

        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void noData() {
        mSwipeRefreshLayoutMain.setRefreshing(false);
        mLoadingCpv.setVisibility(View.GONE);
        mRecyclerMain.setVisibility(View.GONE);
        mNoDataView.setVisibility(View.VISIBLE);
        mDataLoadingErrorView.setVisibility(View.GONE);
    }

    // BudgetItemsAdapterListener implementation

    @Override
    public void onItemClick(BudgetItemModel item, int position) {
        if (mActionMode != null) {
            mBudgetItemsAdapter.toggleItem(position);

            int selectedItemsCount = mBudgetItemsAdapter.getSelectedCount();
            if (selectedItemsCount < 1) {
                mActionMode.finish();
                mActionMode = null;
            } else {
                mActionMode.setTitle(getString(R.string.action_mode_selected_count, String.valueOf(selectedItemsCount)));
            }
        }
    }

    @Override
    public void onItemLongClick(BudgetItemModel item, int position) {
        if (mActionMode == null) {
            getActivity().startActionMode(this);
        }
        mBudgetItemsAdapter.toggleItem(position);

        if (mActionMode != null) {
            int selectedItemsCount = mBudgetItemsAdapter.getSelectedCount();
            if (selectedItemsCount < 1) {
                mActionMode.finish();
            } else {
                mActionMode.setTitle(getString(R.string.action_mode_selected_count, String.valueOf(selectedItemsCount)));
            }
        }
    }

    // ActionMode.Callback implementation

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mActionMode = mode;

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getActivity());
        menuInflater.inflate(R.menu.menu_action_mode, menu);

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_action_mode_remove:
                new AlertDialog.Builder(getContext())
                        .setMessage(R.string.action_mode_remove_alert_message)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> moneyPresenter.removeItems(mBudgetItemsAdapter.getSelectedItemIds(), mBudgetType, authToken))
                        .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel())
                        .show();
                break;

            default:break;
        }

        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
        mBudgetItemsAdapter.clearSelections();
    }
}
