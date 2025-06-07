package kr.ac.yuhan.cs.androidproject;

import java.util.List;

import kr.ac.yuhan.cs.androidproject.dto.AddGroupMemberRequest;
import kr.ac.yuhan.cs.androidproject.dto.AppointmentRequest;
import kr.ac.yuhan.cs.androidproject.dto.AppointmentResponse;
import kr.ac.yuhan.cs.androidproject.dto.ArrivalLog;
import kr.ac.yuhan.cs.androidproject.dto.CreateGroupRequest;
import kr.ac.yuhan.cs.androidproject.dto.FindIdRequest;
import kr.ac.yuhan.cs.androidproject.dto.FindIdResponse;
import kr.ac.yuhan.cs.androidproject.dto.FriendAcceptDto;
import kr.ac.yuhan.cs.androidproject.dto.FriendRequestDto;
import kr.ac.yuhan.cs.androidproject.dto.GetUserResponse;
import kr.ac.yuhan.cs.androidproject.dto.GroupDetail;
import kr.ac.yuhan.cs.androidproject.dto.GroupSummary;
import kr.ac.yuhan.cs.androidproject.dto.LoginRequest;
import kr.ac.yuhan.cs.androidproject.dto.LoginResponse;
import kr.ac.yuhan.cs.androidproject.dto.NewPasswordRequest;
import kr.ac.yuhan.cs.androidproject.dto.PlaceRequest;
import kr.ac.yuhan.cs.androidproject.dto.PlaceResponse;
import kr.ac.yuhan.cs.androidproject.dto.RegisterRequest;
import kr.ac.yuhan.cs.androidproject.dto.UpdateUserRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<LoginResponse> registerUser(@Body RegisterRequest request);

    @POST("auth/findid")
    Call<FindIdResponse> findUsernameByEmail(@Body FindIdRequest request);

    @GET("users/{username}")
    Call<GetUserResponse> getUserInfo(@Path("username") String username);

    @PUT("/api/auth/newpassword")
    Call<Void> resetPassword(@Body NewPasswordRequest request);

    @PUT("users/{username}")
    Call<Void> updateUser(@Path("username") String username, @Body UpdateUserRequest request );

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

    @GET("groups")
    Call<List<GroupSummary>> getGroups();

    @GET("groups/{id}")
    Call<GroupDetail> getGroupDetail(@Path("id") long groupId);

    @POST("groups")
    Call<Void> createGroup(@Body CreateGroupRequest request);

    @POST("groups/{id}/members")
    Call<Void> addGroupMember(@Path("id") long groupId, @Body AddGroupMemberRequest request);

    // 약속 생성
    @POST("appointments")
    Call<AppointmentResponse> createAppointment(@Body AppointmentRequest appointmentRequest);

    // 약속 단건 조회
    @GET("appointments/{id}")
    Call<AppointmentResponse> getAppointment(@Path("id") Long id);

    // 약속 수정
    @PUT("appointments/{id}")
    Call<Void> updateAppointment(@Path("id") Long id, @Body AppointmentRequest appointmentRequest);

    // 약속 삭제
    @DELETE("appointments/{id}")
    Call<Void> deleteAppointment(@Path("id") Long id);

    // 전체 약속 조회 (관리자 권한용)
    @GET("appointments")
    Call<List<AppointmentResponse>> getAllAppointments();

    // 특정 약속 도착 로그 조회
    @GET("appointments/{id}/arrivals")
    Call<List<ArrivalLog>> getArrivalLogs(@Path("id") Long id);

    // 특정 약속에 도착 표시 (체크인)
    @POST("appointments/{id}/arrive")
    Call<Void> arriveAtAppointment(@Path("id") Long id);

    // 로그인된 사용자의 약속 목록 조회
    @GET("appointments/me")
    Call<List<AppointmentResponse>> getMyAppointments();

    @POST("/places")
    Call<PlaceResponse> createOrGetPlace(@Body PlaceRequest placeRequest);

    @GET("/places/{id}")
    Call<PlaceResponse> getPlaceById(@Path("id") Long id);
}
