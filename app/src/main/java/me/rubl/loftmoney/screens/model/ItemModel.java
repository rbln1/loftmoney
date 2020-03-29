package me.rubl.loftmoney.screens.model;

import java.io.Serializable;

public class ItemModel implements Serializable {

    public static final String KEY_NAME = ItemModel.class.getName();

    private String name;
    private String value;
    private BudgetType type;

    public enum BudgetType {
        EXPENSE,
        INCOME,
        BALANCE
    }


    public ItemModel(String name, String value, BudgetType type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public BudgetType getType() {
        return type;
    }
}
