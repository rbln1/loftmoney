package me.rubl.loftmoney.screens.web;

import me.rubl.loftmoney.screens.LoftApp;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebFactory {

    private static WebFactory sInstance = null;
    private Retrofit mRetrofit;
    private OkHttpClient mHttpClient;

    public static WebFactory getInstance() {
        if (sInstance == null) sInstance = new WebFactory();
        return sInstance;
    }

    private WebFactory() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        mHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(mHttpClient)
                .baseUrl(LoftApp.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public LoftApi getApi() {
        return mRetrofit.create(LoftApi.class);
    }

}
