package me.rubl.loftmoney.screens.screens.add_item;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.rubl.loftmoney.R;
import me.rubl.loftmoney.screens.screens.main.fragment.BudgetFragment;
import me.rubl.loftmoney.screens.screens.main.model.ItemModel;
import me.rubl.loftmoney.screens.web.WebFactory;
import me.rubl.loftmoney.screens.web.model.AuthResponse;

public class AddItemActivity extends AppCompatActivity {

    private ItemModel.BudgetType mBudgetItemType;

    private List<Disposable> mDisposableList = new ArrayList<>();

    private TextInputEditText mNameET;
    private TextInputEditText mPriceET;
    private Button mAddBtn;

    private int mTextColor;
    private String mName;
    private String mPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mNameET = findViewById(R.id.et_add_item_name);
        mPriceET = findViewById(R.id.et_add_item_price);
        mAddBtn = findViewById(R.id.btn_add_item);

        if (getIntent() != null) {
            mBudgetItemType = (ItemModel.BudgetType) getIntent().getSerializableExtra(BudgetFragment.KEY_BUDGET_TYPE);
            mTextColor = mBudgetItemType == ItemModel.BudgetType.EXPENSE
                    ? getResources().getColor(R.color.light_expenses_price_text_color)
                    : getResources().getColor(R.color.light_incomes_value_text_color);
            mNameET.setTextColor(mTextColor);
            mPriceET.setTextColor(mTextColor);
            mAddBtn.setTextColor(mTextColor);
        }

        mNameET.addTextChangedListener(nameTextWatcher);
        mPriceET.addTextChangedListener(priceTextWatcher);

        mAddBtn.setOnClickListener((view) -> {

            if (TextUtils.isEmpty(mName) && TextUtils.isEmpty(mPrice)) return;

            addNewItem(Integer.valueOf(mPrice), mName.trim());
            finish();
        });
    }

    private void setLoading(Boolean state) {
        mNameET.setEnabled(!state);
        mPriceET.setEnabled(!state);
        mAddBtn.setVisibility(state ? View.GONE : View.VISIBLE);
    }

    private void addNewItem(Integer price, String name) {

        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        String authToken = preferences.getString(AuthResponse.AUTH_TOKEN_KEY, "");

        setLoading(true);

        Disposable addItemRequest = WebFactory.getInstance().getApi().addItem(price, name, mBudgetItemType.getStringType(), authToken)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        Toast.makeText(getApplicationContext(), getString(R.string.item_successfully_added), Toast.LENGTH_LONG).show();
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        setLoading(false);
                        Toast.makeText(getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        mDisposableList.add(addItemRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();

        for (Disposable disposable : mDisposableList) {
            disposable.dispose();
        }
    }

    TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mName = s.toString();
            checkEditTextHasText();
        }
    };

    TextWatcher priceTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mPrice = s.toString();
            checkEditTextHasText();
        }
    };

    public void checkEditTextHasText() {
        mAddBtn.setEnabled(!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mPrice));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }
}
