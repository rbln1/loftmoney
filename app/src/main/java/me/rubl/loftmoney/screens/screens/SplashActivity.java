package me.rubl.loftmoney.screens.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.rubl.loftmoney.R;
import me.rubl.loftmoney.screens.screens.auth.AuthActivity;
import me.rubl.loftmoney.screens.screens.main.MainActivity;
import me.rubl.loftmoney.screens.web.model.AuthResponse;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        String authToken = preferences.getString(AuthResponse.AUTH_TOKEN_KEY, "");

        if (TextUtils.isEmpty(authToken)) {
            startActivity(new Intent(this, AuthActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }

        finish();
    }
}
