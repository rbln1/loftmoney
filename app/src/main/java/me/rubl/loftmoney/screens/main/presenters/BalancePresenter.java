package me.rubl.loftmoney.screens.main.presenters;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.rubl.loftmoney.common.BudgetType;
import me.rubl.loftmoney.common.web.WebFactory;
import me.rubl.loftmoney.common.web.model.BudgetItemResponseModel;
import me.rubl.loftmoney.screens.main.model.BudgetItemModel;
import me.rubl.loftmoney.screens.main.view_state.BalanceViewState;

public class BalancePresenter {

    private List<Disposable> mDisposables = new ArrayList<>();
    private BalanceViewState balanceViewState = null;

    public void setBalanceViewState(BalanceViewState balanceViewState) {
        this.balanceViewState = balanceViewState;
    }

    public void onDestroy() {
        for(Disposable disposable : mDisposables) {
            disposable.dispose();
        }
    }

    public void fetchBalance(String auth_token) {

        Single<List<BudgetItemResponseModel>> expenseRequest = WebFactory.getInstance().getApi().getItems(BudgetType.EXPENSE.getStringType(), auth_token);
        Single<List<BudgetItemResponseModel>> incomeRequest = WebFactory.getInstance().getApi().getItems(BudgetType.INCOME.getStringType(), auth_token);

        balanceViewState.setState(true);

        Disposable disposable = Single.zip(expenseRequest, incomeRequest, (expenses, incomes) -> {

            List<BudgetItemModel> totalModels = new ArrayList<>(expenses.size() + incomes.size());

            for (BudgetItemResponseModel itemRemote : expenses) {
                totalModels.add(new BudgetItemModel(itemRemote));
            }
            for (BudgetItemResponseModel itemRemote : incomes) {
                totalModels.add(new BudgetItemModel(itemRemote));
            }

            return totalModels;
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(budgetItemModels -> {
                    int balance = 0;
                    int income = 0;
                    int expense = 0;

                    for (BudgetItemModel itemModel : budgetItemModels) {
                        if (itemModel.getType() == BudgetType.EXPENSE) {
                            expense += itemModel.getValue();
                            balance -= itemModel.getValue();
                        }
                    }
                    for (BudgetItemModel itemModel : budgetItemModels) {
                        if (itemModel.getType() == BudgetType.INCOME) {
                            income += itemModel.getValue();
                            balance += itemModel.getValue();
                        }
                    }

                    balanceViewState.setState(false);

                    balanceViewState.setBalance(balance + "₽");
                    balanceViewState.setExpense(expense + "₽");
                    balanceViewState.setIncome(income + "₽");
                    balanceViewState.setDiagram((float) expense, (float) income);
                }, Throwable::printStackTrace);

        mDisposables.add(disposable);
    }
}
