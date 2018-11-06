package com.smartgreenhouse.alphagrow.services;

import com.smartgreenhouse.alphagrow.models.Login;
import com.smartgreenhouse.alphagrow.models.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UsuarioService {

    @POST("autenticacao/")
    Call<String> autenticar(@Body Login login);

    @GET("autenticacao/obterUsuario")
    Call<Usuario> obeterUsuario(@Query("idUsuario") String id);

    @GET("autenticacao")
    Call<Login> obeterLogin(@Query("idLogin") String id);

}
