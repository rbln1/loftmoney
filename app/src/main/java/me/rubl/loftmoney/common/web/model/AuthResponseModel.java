package me.rubl.loftmoney.common.web.model;

import com.google.gson.annotations.SerializedName;

public class AuthResponseModel {

    public static final String AUTH_TOKEN_KEY = "auth-token-key";

    private String status;
    @SerializedName("id")
    private String userId;
    @SerializedName("auth_token")
    private String authToken;

    public String getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    public String getAuthToken() {
        return authToken;
    }
}
