package me.rubl.loftmoney.screens.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import me.rubl.loftmoney.R;
import me.rubl.loftmoney.views.DiagramView;
import me.rubl.loftmoney.screens.main.presenters.BalancePresenter;
import me.rubl.loftmoney.screens.main.view_state.BalanceViewState;
import me.rubl.loftmoney.common.web.model.AuthResponseModel;

public class BalanceFragment extends Fragment implements BalanceViewState {

    private BalancePresenter balancePresenter;
    private String authToken;

    private AppCompatTextView mAvailableMoneyTitleTv;
    private AppCompatTextView mAvailableMoneyValueTv;
    private AppCompatTextView mExpensesTitleTv;
    private AppCompatTextView mExpenseValueTv;
    private AppCompatTextView mIncomesTitleTv;
    private AppCompatTextView mIncomeValueTv;
    private View mDivider1, mDivider2, mDivider3;
    private CircularProgressView mLoadingCpv;
    private DiagramView diagramView;

    public static BalanceFragment getInstance() {
        return new BalanceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAvailableMoneyTitleTv = view.findViewById(R.id.fragment_balance_available_money_title);
        mAvailableMoneyValueTv = view.findViewById(R.id.fragment_balance_available_money_value);
        mExpensesTitleTv = view.findViewById(R.id.fragment_balance_expense_title);
        mExpenseValueTv = view.findViewById(R.id.fragment_balance_expense_value);
        mIncomesTitleTv = view.findViewById(R.id.fragment_balance_income_title);
        mIncomeValueTv = view.findViewById(R.id.fragment_balance_income_value);
        mDivider1 = view.findViewById(R.id.fragment_balance_divider1);
        mDivider2 = view.findViewById(R.id.fragment_balance_divider2);
        mDivider3 = view.findViewById(R.id.fragment_balance_divider3);
        mLoadingCpv = view.findViewById(R.id.fragment_balance_cpv_loading);
        diagramView = view.findViewById(R.id.fragment_balance_diagram);

        balancePresenter = new BalancePresenter();
        balancePresenter.setBalanceViewState(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (TextUtils.isEmpty(authToken)) {
            SharedPreferences preferences = getActivity().getSharedPreferences(getActivity().getString(R.string.app_name), Context.MODE_PRIVATE);
            authToken = preferences.getString(AuthResponseModel.AUTH_TOKEN_KEY, "");
        }

        fetchBalance();
    }

    @Override
    public void onStop() {
        super.onStop();

        balancePresenter.onDestroy();
    }

    public void fetchBalance() {
        if (getActivity() == null) return;

        if (TextUtils.isEmpty(authToken)) return;

        balancePresenter.fetchBalance(authToken);
    }

    // BalanceViewState implementation

    @Override
    public void setState(Boolean isLoading) {
        mAvailableMoneyTitleTv.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mAvailableMoneyValueTv.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mExpensesTitleTv.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mExpenseValueTv.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mIncomesTitleTv.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mIncomeValueTv.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mDivider1.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mDivider2.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mDivider3.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mLoadingCpv.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        diagramView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setBalance(String balance) {
        mAvailableMoneyValueTv.setText(balance);
    }

    @Override
    public void setExpense(String expense) {
        mExpenseValueTv.setText(expense);
    }

    @Override
    public void setIncome(String income) {
        mIncomeValueTv.setText(income);
    }

    @Override
    public void setDiagram(float expense, float income) {
        diagramView.update(expense, income);
    }
}
