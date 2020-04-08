package me.rubl.loftmoney.screens.web.model;

import java.util.List;

public class GetItemsResponse {

    private String status;
    private List<ItemRemote> data;

    public String getStatus() {
        return status;
    }

    public List<ItemRemote> getData() {
        return data;
    }
}