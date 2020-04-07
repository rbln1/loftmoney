package me.rubl.loftmoney.screens.screens.main.interfaces;

import me.rubl.loftmoney.screens.screens.main.model.ItemModel;

public interface ItemsAdapterListener {

    void onItemClick(ItemModel item, int position);

    void onItemLongClick(ItemModel item, int position);

}
