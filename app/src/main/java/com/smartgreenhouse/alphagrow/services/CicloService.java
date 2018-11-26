package com.smartgreenhouse.alphagrow.services;

import com.smartgreenhouse.alphagrow.models.Ciclo;
import com.smartgreenhouse.alphagrow.models.Cultivo;

import retrofit2.Call;
import retrofit2.http.POST;

public interface CicloService {

    @POST("ciclo/")
    Call<Ciclo> salvarCiclo(Ciclo ciclo);
}
