package me.rubl.loftmoney.screens.web;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import me.rubl.loftmoney.screens.web.model.AuthResponse;
import me.rubl.loftmoney.screens.web.model.ItemRemote;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoftApi {

    @GET("./items")
    Single<List<ItemRemote>> getItems(@Query("type") String type, @Query("auth-token") String auth_token);

    @POST("./items/add")
    @FormUrlEncoded
    Completable addItem(@Field("price") Integer price, @Field("name") String name,
                         @Field("type") String type, @Field("auth-token") String authToken);

    @GET("./auth")
    Single<AuthResponse> auth(@Query("social_user_id") String userId);

}
