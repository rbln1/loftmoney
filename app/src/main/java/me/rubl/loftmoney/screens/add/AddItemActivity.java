package me.rubl.loftmoney.screens.add;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.rubl.loftmoney.R;
import me.rubl.loftmoney.common.BudgetType;
import me.rubl.loftmoney.common.web.WebFactory;
import me.rubl.loftmoney.common.web.model.AuthResponseModel;
import me.rubl.loftmoney.screens.main.fragment.BudgetFragment;

public class AddItemActivity extends AppCompatActivity {

    private BudgetType mBudgetItemType;
    private List<Disposable> mDisposableList = new ArrayList<>();
    private Drawable mAddBtnIconDrawable;
    private int addBtnTextColor;
    private String mName;
    private String mPrice;

    private TextInputEditText mNameET;
    private TextInputEditText mPriceET;
    private Button mAddBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mNameET = findViewById(R.id.activity_add_item_et_name);
        mPriceET = findViewById(R.id.activity_add_item_et_price);
        mAddBtn = findViewById(R.id.activity_add_item_btn_add);

        if (getIntent() != null) {
            mBudgetItemType = (BudgetType) getIntent().getSerializableExtra(BudgetFragment.KEY_BUDGET_TYPE);

            if (mBudgetItemType != null) {
                switch (mBudgetItemType) {
                    case EXPENSE:
                        mAddBtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_add_button_expense_text_color));
                        addBtnTextColor = getResources().getColor(R.color.accept_color_expense);
                        mAddBtnIconDrawable = getResources().getDrawable(R.drawable.selector_add_expense_button);
                        break;

                    case INCOME:
                        mAddBtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_add_button_income_text_color));
                        addBtnTextColor = getResources().getColor(R.color.accept_color_income);
                        mAddBtnIconDrawable = getResources().getDrawable(R.drawable.selector_add_income_button);
                        break;

                    default:break;
                }
            }

            if (mAddBtnIconDrawable != null) {
                mAddBtnIconDrawable.mutate();
                mAddBtnIconDrawable.setBounds(0, 0, 60, 60);

                mAddBtn.setCompoundDrawables(mAddBtnIconDrawable, null, null, null);
            }

            if (addBtnTextColor != 0) {
                mNameET.setTextColor(addBtnTextColor);
                mPriceET.setTextColor(addBtnTextColor);
            }
        }

        mNameET.addTextChangedListener(validateTextWatcher);
        mPriceET.addTextChangedListener(validateTextWatcher);

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
        String authToken = preferences.getString(AuthResponseModel.AUTH_TOKEN_KEY, "");

        setLoading(true);

        Disposable addItemRequest = WebFactory.getInstance().getApi().addItem(price, name, mBudgetItemType.getStringType(), authToken)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toast.makeText(getApplicationContext(), getString(R.string.successfully_added), Toast.LENGTH_LONG).show();
                    finish();
                }, throwable -> {
                    setLoading(false);
                    Toast.makeText(getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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

    TextWatcher validateTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mName = mNameET.getText().toString();
            mPrice = mPriceET.getText().toString();

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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fadeout, R.anim.fadein);
    }
}
