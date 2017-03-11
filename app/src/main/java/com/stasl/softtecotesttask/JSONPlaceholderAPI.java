package com.stasl.softtecotesttask;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JSONPlaceholderAPI {
    @GET("posts")
    Call<List<PostModel>> getPosts();
    @GET("users")
    Call<List<UserModel>> getUsers();
    @GET("posts/{number}")
    Call<PostModel> getPost(@Path("number") int number);
    @GET("users/{number}")
    Call<UserModel> getUser(@Path("number") int number);
}
