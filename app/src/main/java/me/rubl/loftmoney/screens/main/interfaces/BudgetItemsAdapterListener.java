package me.rubl.loftmoney.screens.main.interfaces;

import me.rubl.loftmoney.screens.main.model.BudgetItemModel;

public interface BudgetItemsAdapterListener {

    void onItemClick(BudgetItemModel item, int position);

    void onItemLongClick(BudgetItemModel item, int position);

}
