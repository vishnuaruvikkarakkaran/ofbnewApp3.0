package com.enfin.ofabee3.data.remote;


import com.enfin.ofabee3.data.remote.model.Response.UserLocationModel;
import com.enfin.ofabee3.data.remote.model.removecoupon.response.CouponRemoveResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface WebApiListener {

    @Headers("Content-Type: application/json")
    @GET("/mobile_api/current_location")
    Call<UserLocationModel> getLocation();

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/signup")
    Call<String> registerUser(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/login")
    Call<String> userLogin(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/otp_verification")
    Call<String> validateOtp(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/resend_otp")
    Call<String> reSendOtp(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/home")
    Call<String> home(@Header("Authorization") String authToken, @Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/home")
    Call<String> home(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @GET("/mobile_api/my_courses")
    Call<String> mycourses(@Header("Authorization") String authToken);

    @Headers("Content-Type: application/json")
    @GET("/mobile_api/profile")
    Call<String> myprofile(@Header("Authorization") String authToken);

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/forgot_password")
    Call<String> forgotPasswordGetOTP(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST(".")
    Call<String> resetPassword(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/save_categories")
    Call<String> savecourseCategory(@Header("Authorization") String authToken, @Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @GET("/mobile_api/categories")
    Call<String> courseCategory(@Header("Authorization") String authToken);

    @Headers("Content-Type: application/json")
    @GET("/mobile_api/categories")
    Call<String> courseCategory();

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/more_items")
    Call<String> seeallcourses(@Header("Authorization") String authToken, @Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/more_items")
    Call<String> seeallcourses(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/explore_courses")
    Call<String> explorecourses(@Header("Authorization") String authToken, @Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/explore_courses")
    Call<String> explorecourses(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/course_info")
    Call<String> coursedetail(@Header("Authorization") String authToken, @Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/payment_request")
    Call<String> createOrder(@Header("Authorization") String authToken, @Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/free_enroll")
    Call<String> selfEnroll(@Header("Authorization") String authToken, @Body RequestBody requestBody);


    @Headers("Content-Type: application/json")
    @POST("/mobile_api/payment_response")
    Call<String> completePayment(@Header("Authorization") String authToken, @Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/check_coupon")
    Call<String> applycoupon(@Header("Authorization") String authToken, @Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @GET("/mobile_api/reset_coupon")
    Call<String> removecoupon(@Header("Authorization") String authToken);

    @Headers("Content-Type: application/json")
    @GET("/mobile_api/signout")
    Call<String> logout(@Header("Authorization") String authToken);

    //@Headers("Content-Type: application/json")
    @Multipart
    @POST("/mobile_api/upload_user_image")
    Call<String> imageupload(@Header("Authorization") String authToken, @Part MultipartBody.Part file);

    @Headers("Content-Type: application/json")
    @POST("/mobile_api/edit_profile")
    Call<String> updateUserProfile(@Header("Authorization") String authToken, @Body RequestBody requestBody);


}
