package me.rubl.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button mAddItemBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddItemBtn = findViewById(R.id.add_item_btn);

        mAddItemBtn.setOnClickListener((v) -> {
            startActivity(new Intent(getApplicationContext(), AddItemActivity.class));
        });

    }
}
