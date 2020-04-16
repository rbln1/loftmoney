package me.rubl.loftmoney.common.web.model;

public class BudgetItemResponseModel {

    private Integer id;
    private String name;
    private Integer price;
    private String type;
    private String date;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
