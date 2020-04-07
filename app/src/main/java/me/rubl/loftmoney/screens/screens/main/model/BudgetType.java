package me.rubl.loftmoney.screens.screens.main.model;

public enum BudgetType {
    EXPENSE("expense"),
    INCOME("income"),
    BALANCE("balance");

    String type;

    BudgetType(String type) {
        this.type = type;
    }

    public String getStringType() {
        return type;
    }
}
