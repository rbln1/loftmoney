package me.rubl.loftmoney.common.web;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import me.rubl.loftmoney.common.web.model.AuthResponseModel;
import me.rubl.loftmoney.common.web.model.BudgetItemResponseModel;
import me.rubl.loftmoney.common.web.model.Status;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoftApi {

    @GET("./auth")
    Single<AuthResponseModel> auth(@Query("social_user_id") String userId);

    @GET("./items")
    Single<List<BudgetItemResponseModel>> getItems(@Query("type") String type, @Query("auth-token") String auth_token);

    @POST("./items/add")
    @FormUrlEncoded
    Completable addItem(@Field("price") Integer price, @Field("name") String name,
                         @Field("type") String type, @Field("auth-token") String authToken);

    @POST("./items/remove")
    @FormUrlEncoded
    Single<Status> removeItem(@Query("id") String id, @Field("auth-token") String auth_token);

}
