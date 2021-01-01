package com.esprit.tourapp.Retrofite;

import com.esprit.tourapp.Sortiee;
import com.esprit.tourapp.endroitt;

import java.util.List;

import io.reactivex.Observable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface INodeJS {
    @POST("Register")
    @FormUrlEncoded
    Observable <String> registerUser(@Field("email") String email,
                                     @Field("nom") String nom,
                                     @Field("password") String password);
    @POST("login")
    @FormUrlEncoded
    Observable <String> loginUser(@Field("email") String email,
                                  @Field("password") String password);
    @GET("sortie")
    Call<List<Sortiee>> getSortie();

    @POST("sorti")
    @FormUrlEncoded
    Observable <String> addSortie(@Field("dt_debut") String dt_sortie,
                                  @Field("dt_fin") String dt_fin,
                                  @Field("location") String location,
                                  @Field("user_email") String user_email );

    @GET("/sortie/{id}")
    Call<Void> DeleteSortie(@Path("id") int id );

    @POST("pendroit")
    @FormUrlEncoded
    Observable <String> addendroit(@Field("nom") String nom,
                                  @Field("location") String location,
                                  @Field("photo") String photo,
                                   @Field("lat") String lat,
                                   @Field("lan") String lan);

    @Multipart
    @POST("/uploadfile")
    Call<String> postImage(@Part MultipartBody.Part image, @Part("myFile") RequestBody name);

    @GET("gendroit")
    Call<List<endroitt>> getEndroit();

}
