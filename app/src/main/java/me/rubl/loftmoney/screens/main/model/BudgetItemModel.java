package me.rubl.loftmoney.screens.main.model;

import java.io.Serializable;

import me.rubl.loftmoney.common.BudgetType;
import me.rubl.loftmoney.common.web.model.BudgetItemResponseModel;

public class BudgetItemModel implements Serializable {

    public static final String KEY_NAME = BudgetItemModel.class.getName();

    private Integer id;
    private String name;
    private Integer value;
    private BudgetType type;
    private String date;

    public BudgetItemModel(BudgetItemResponseModel item) {
        this.id = item.getId();
        this.name = item.getName();
        this.value = item.getPrice();
        this.date = item.getDate();

        switch (item.getType()) {
            case "expense":
                type = BudgetType.EXPENSE;
                break;

            case "income":
                type = BudgetType.INCOME;
                break;

            default:break;
        }
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }
    public String getStringValue() {
        return String.valueOf(value);
    }

    public BudgetType getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
