package com.smartgreenhouse.alphagrow.services;

import com.smartgreenhouse.alphagrow.models.Cultivo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CultivoService {

    @GET("cultivo/obterCultivo")
    Call<Cultivo> obterCultivo(@Query("id") String id);

    @POST("cultivo/")
    Call<Cultivo> salvarCultivo(Cultivo cultivo);
}
