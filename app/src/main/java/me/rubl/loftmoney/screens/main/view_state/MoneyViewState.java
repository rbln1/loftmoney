package me.rubl.loftmoney.screens.main.view_state;

import java.util.List;

import me.rubl.loftmoney.screens.main.model.BudgetItemModel;

public interface MoneyViewState {

    void startLoading();
    void showData(List<BudgetItemModel> data);
    void showError(String error);
    void noData();

}
