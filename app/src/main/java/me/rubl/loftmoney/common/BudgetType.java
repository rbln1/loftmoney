package me.rubl.loftmoney.common;

public enum BudgetType {
    EXPENSE("expense"),
    INCOME("income");

    String type;

    BudgetType(String type) {
        this.type = type;
    }

    public String getStringType() {
        return type;
    }
}
