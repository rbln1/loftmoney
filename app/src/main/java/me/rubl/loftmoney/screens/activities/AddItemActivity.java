package me.rubl.loftmoney.screens.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.rubl.loftmoney.R;
import me.rubl.loftmoney.screens.fragments.BudgetFragment;
import me.rubl.loftmoney.screens.model.ItemModel;

public class AddItemActivity extends AppCompatActivity {

    EditText mNameET;
    EditText mValueET;
    Button mAddBtn;

    private ItemModel.BudgetType mBudgetItemType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        if (getIntent() != null) {
            mBudgetItemType = (ItemModel.BudgetType) getIntent().getSerializableExtra(BudgetFragment.KEY_BUDGET_TYPE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNameET = findViewById(R.id.et_add_item_name);
        mValueET = findViewById(R.id.et_add_item_value);
        mAddBtn = findViewById(R.id.btn_add_item);

        mNameET.addTextChangedListener(textWatcher);
        mValueET.addTextChangedListener(textWatcher);

        mAddBtn.setOnClickListener((view) -> {
            String name = mNameET.getText().toString().trim();
            String value = mValueET.getText().toString();

            if (TextUtils.isEmpty(name) && TextUtils.isEmpty(value)) return;

            ItemModel model = new ItemModel(name, value, mBudgetItemType);

            Intent modelIntent = new Intent();
            modelIntent.putExtra(ItemModel.KEY_NAME, model);
            setResult(RESULT_OK, modelIntent);
            finish();
        });

    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(mNameET.getText().toString()) && TextUtils.isEmpty(mValueET.getText().toString())) {
                mAddBtn.setEnabled(false);
            } else {
                mAddBtn.setEnabled(true);
            }
        }
    };

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
