package tuanbm.hust.service;


import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tuanbm.hust.object.ChangePwdUser;
import tuanbm.hust.object.LoginToken;
import tuanbm.hust.object.LoginUser;
import tuanbm.hust.object.ResponseTags;
import tuanbm.hust.object.ResponseWords;
import tuanbm.hust.object.SignupResponse;
import tuanbm.hust.object.SignupUser;
import tuanbm.hust.object.Word;
import tuanbm.hust.object.WordPost;

public interface RetrofitService {
    @POST("/api/search")
    Call<List<Word>> search(@Query("lex") String searchedWord);
    @GET("/api/e_search?")
    Call<List<Word>> getAutoComplete(@Query("q") String currentSearch);
    @Headers({
            "Content-Type:application/json",
            "X-Requested-With:XMLHttpRequest"
    })
    @POST("api/auth/login")
    Call<LoginToken> login(@Body LoginUser user);
    @Headers({
            "Content-Type:application/json",
            "X-Requested-With:XMLHttpRequest"
    })
    @POST("api/auth/signup")
    Call<SignupResponse> signup(@Body SignupUser user);

    @GET("api/auth/logout")
    Call<LoginToken> logout(@Header("Authorization") String authHeader);
    @GET("api/user_word/show_list")
    Call<ResponseTags> getTags(@Header("Authorization") String authHeader);
    @POST("api/user_word/show_list_word")
    Call<List<String>> getWords(@Header("Authorization") String authHeader, @Body WordPost wordPost);
    @Headers({
            "Content-Type:application/x-www-form-urlencoded",
    })
    @POST("api/user_word/edit")
    Call<ResponseTags> mark(@Header("Authorization") String authHeader, @Body WordPost wordPost);
    @POST("api/auth/re_pass")
    Call<LoginToken> re_pass(@Header("Authorization") String authHeader, @Body ChangePwdUser changePwdUser);

    @GET("/api/user_word/show")
    Call<JsonObject> getUserWord(@Header("Authorization") String authHeader);
}
