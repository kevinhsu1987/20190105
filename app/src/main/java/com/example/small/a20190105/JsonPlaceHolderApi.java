package com.example.small.a20190105;

import com.example.small.a20190105.Model.Login_history;
import com.example.small.a20190105.Model.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {
    String BASE_URL = "http://210.240.245.13:8899/api/";

    @GET("users/")
    Call<List<Student>> getStudent();

    @POST("users/")
    Call<Student> PostStudent(@Body Student student);

    @GET("login_history/")
    Call<List<Login_history>> getLoginHistory();

    @POST("login_history/")
    Call<Login_history> PostLoginHistory(@Body Login_history login_history);
}
