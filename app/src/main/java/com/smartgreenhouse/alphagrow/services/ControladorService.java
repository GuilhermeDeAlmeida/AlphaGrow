package com.smartgreenhouse.alphagrow.services;

import com.smartgreenhouse.alphagrow.models.Controlador;
import com.smartgreenhouse.alphagrow.models.Cultivo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ControladorService {

    @GET("controlador/controladores/")
    Call<ArrayList<Controlador>> obterControladores();

    @GET("cultivo/obterCultivo")
    Call<Cultivo> obterCultivo(@Query("idCultivo") String id);

}
