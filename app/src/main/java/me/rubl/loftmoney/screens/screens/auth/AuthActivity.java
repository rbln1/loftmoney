package me.rubl.loftmoney.screens.screens.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.rubl.loftmoney.R;
import me.rubl.loftmoney.screens.screens.main.MainActivity;
import me.rubl.loftmoney.screens.web.WebFactory;
import me.rubl.loftmoney.screens.web.model.AuthResponse;

public class AuthActivity extends AppCompatActivity {

    Button mSignInBtn;

    List<Disposable> disposables = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mSignInBtn = findViewById(R.id.btn_auth_sign_in);
        mSignInBtn.setOnClickListener((view) -> {
            startSignIn();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
    }

    private void startSignIn() {

        mSignInBtn.setVisibility(View.INVISIBLE);

        Disposable authRequest = WebFactory.getInstance().getApi().auth("rbln1")
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<AuthResponse>() {
                @Override
                public void accept(AuthResponse authResponse) throws Exception {

                    SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name),
                            Context.MODE_PRIVATE);
                    preferences.edit().putString(AuthResponse.AUTH_TOKEN_KEY, authResponse.getAuthToken()).apply();

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    mSignInBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Error: " + throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        disposables.add(authRequest);
    }
}
