package kr.ac.yuhan.cs.androidproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<LoginResponse> registerUser(@Body RegisterRequest request);

    @GET("users/{username}")
    Call<GetUserResponse> getUserInfo(
            @Header("Authorization") String token,
            @Path("username") String username
    );

    @PUT("users/{username}")
    Call<Void> updateUser(
            @Header("Authorization") String token,
            @Path("username") String username,
            @Body UpdateUserRequest request
    );

    // 1. 친구 요청 보내기
    @POST("friends/request")
    Call<Void> sendFriendRequest(@Body FriendRequestDto friendRequest);

    // 2. 친구 요청 수락
    @POST("friends/accept")
    Call<Void> acceptFriendRequest(@Body FriendAcceptDto friendAccept);

    // 3. 친구 요청 거절
    @POST("friends/reject")
    Call<Void> rejectFriendRequest(@Body FriendRequestDto friendReject);

    // 4. 친구 목록 조회
    @GET("friends")
    Call<List<GetUserResponse>> getFriends();

    @GET("friends/requests")
    Call<List<GetUserResponse>> getFriendRequests();
}
