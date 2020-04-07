package me.rubl.loftmoney.screens.screens.main.model;

import java.io.Serializable;

import me.rubl.loftmoney.screens.web.model.ItemRemote;

public class ItemModel implements Serializable {

    public static final String KEY_NAME = ItemModel.class.getName();

    private String id;
    private String name;
    private String value;
    private BudgetType type;
    private String date;

    public ItemModel(ItemRemote item) {
        this.id = item.getId();
        this.name = item.getName();
        this.value = String.valueOf(item.getPrice());
        this.date = item.getDate();

        switch (item.getType()) {
            case "expense":
                type = BudgetType.EXPENSE;
                break;
            case "income":
                type = BudgetType.INCOME;
                break;
            case "balance":
                type = BudgetType.BALANCE;
                break;
            default:break;
        }
    }

    public String getId() {
        return id;
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

    public String getDate() {
        return date;
    }
}
