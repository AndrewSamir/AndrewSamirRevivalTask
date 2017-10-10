package com.test.ksi.testmap.retorfitconfig;

import com.test.ksi.testmap.models.googlePlacesApis.ModelGooglePlacesApis;
import com.test.ksi.testmap.utilities.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;




public interface ApiCall {

    @GET(Constant.subUrl + "maps/api/place/nearbysearch/json?")
    Call<ModelGooglePlacesApis> getGooglePlacesCall(
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("key") String key);

}