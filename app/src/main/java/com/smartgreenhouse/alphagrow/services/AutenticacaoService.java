package com.smartgreenhouse.alphagrow.services;

import com.smartgreenhouse.alphagrow.models.Autenticacao;
import com.smartgreenhouse.alphagrow.models.Controlador;
import com.smartgreenhouse.alphagrow.models.Cultivo;
import com.smartgreenhouse.alphagrow.models.Login;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AutenticacaoService {

    @POST("autenticacao")
    Call<Autenticacao> autenticar(@Body Login login);

}
