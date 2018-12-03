package com.smartgreenhouse.alphagrow.services;

import com.smartgreenhouse.alphagrow.models.Atuador;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AtuadorService {
    @POST("atuadores/")
    Call<Boolean> salvarEstadoAtuador(@Body Atuador atuador);

    @GET("atuadores/")
    Call<List<Atuador>> obterEstadoAtuadores(@Query("token") String token);


}
