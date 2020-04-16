package me.rubl.loftmoney.screens.main.view_state;

public interface BalanceViewState {

    void setState(Boolean isLoading);
    void setBalance(String balance);
    void setExpense(String expense);
    void setIncome(String income);
    void setDiagram(float expense, float income);

}
