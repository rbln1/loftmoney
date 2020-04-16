package me.rubl.loftmoney.screens.main.presenters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.rubl.loftmoney.common.BudgetType;
import me.rubl.loftmoney.common.web.WebFactory;
import me.rubl.loftmoney.common.web.model.BudgetItemResponseModel;
import me.rubl.loftmoney.screens.main.model.BudgetItemModel;
import me.rubl.loftmoney.screens.main.view_state.MoneyViewState;

public class MoneyPresenter {

    private List<Disposable> mDisposables = new ArrayList<>();
    private MoneyViewState moneyViewState = null;

    public void setMoneyViewState(MoneyViewState main_view_state) {
        moneyViewState = main_view_state;
    }

    public void onDestroy() {
        for(Disposable disposable : mDisposables) {
            disposable.dispose();
        }
    }

    public void loadItems(String budget_type, String auth_token) {
        moneyViewState.startLoading();

        Disposable getItemsRequest = WebFactory.getInstance().getApi().getItems(budget_type, auth_token)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemRemotes -> {

                    List<BudgetItemModel> loadedItems = new ArrayList<>();

                    for (BudgetItemResponseModel itemRemote : itemRemotes) {
                        loadedItems.add(new BudgetItemModel(itemRemote));
                    }

                    if (loadedItems.isEmpty()) {
                        moneyViewState.noData();
                    } else {
                        sortItemsById(loadedItems);
                        moneyViewState.showData(loadedItems);
                    }
                }, throwable -> moneyViewState.showError(throwable.getLocalizedMessage()));

        mDisposables.add(getItemsRequest);
    }

    public void removeItems(List<Integer> selectedItemIds, BudgetType budgetType, String authToken) {

        moneyViewState.startLoading();

        for (Integer itemId : selectedItemIds) {
            Disposable removeBudgetItemRequest = WebFactory.getInstance().getApi().removeItem(String.valueOf(itemId), authToken)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            status -> loadItems(budgetType.getStringType(), authToken),
                            throwable -> moneyViewState.showError(throwable.getLocalizedMessage()))
                    ;
            mDisposables.add(removeBudgetItemRequest);
        }
    }

    private void sortItemsById(List<BudgetItemModel> items) {
        Collections.sort(items, (o1, o2) -> o1.getId().compareTo(o2.getId()));
    }
}
