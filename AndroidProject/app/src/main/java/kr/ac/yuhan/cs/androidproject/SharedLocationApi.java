package kr.ac.yuhan.cs.androidproject;

import java.util.List;

import kr.ac.yuhan.cs.androidproject.dto.SharedLocation;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SharedLocationApi {

    @POST("/shared")
    Call<Void> sendLocation(@Body SharedLocation location);

    @GET("/shared/{appointmentId}")
    Call<List<SharedLocation>> getSharedLocations(@Path("appointmentId") Long appointmentId);
}
