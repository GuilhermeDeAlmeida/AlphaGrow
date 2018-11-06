package com.smartgreenhouse.alphagrow.services;

import com.smartgreenhouse.alphagrow.models.Atuador;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AtuadorService {
    @POST("atuadores/")
    Call<Atuador> salvarEstadoAtuador(Atuador atuador);

    @GET("atuadores/")
    Call<Atuador> obterEstadoAtuadores(String token);
}
