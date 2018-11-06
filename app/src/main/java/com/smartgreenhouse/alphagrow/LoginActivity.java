package com.smartgreenhouse.alphagrow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.smartgreenhouse.alphagrow.config.APIConfig;
import com.smartgreenhouse.alphagrow.models.Login;
import com.smartgreenhouse.alphagrow.services.AutenticacaoService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText senha;
    AutenticacaoService autenticacaoService;
    String autenticado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        autenticacaoService = APIConfig.getClient().create(AutenticacaoService.class);
    }

    public void efetuarLogin(View view){
        //Pegando os valores da view activity_login, atravas de seu ID
        email = (EditText) findViewById(R.id.editTextUserEmail);
        senha = (EditText) findViewById(R.id.editTextPassword);

        Login login = new Login(email.getText().toString(), senha.getText().toString());
        usuarioAutenticado(login);

    }

    private String usuarioAutenticado(Login login) {
        Call<String> call = autenticacaoService.autenticar(login);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("RESPOSTA","Resposta do WebService: " + response.body());
                autenticado = response.body();
                if( autenticado == null || !Boolean.parseBoolean(autenticado) == false) {
                    Toast.makeText(getApplicationContext(), "Usuário e/ou senha inválido(s)", Toast.LENGTH_LONG).show();
                }else{
                    validar();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("RESPOSTA","Falhou a request");
                Log.e("RESPOSTA",t.getMessage());
            }
        });
        return autenticado;
    }

    private void validar() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        intent.putExtra("idCultivo", autenticado);
//        intent.putExtra("idUsuario", autenticado);
        intent.putExtra("idLogin", autenticado);
        startActivity(intent);
    }
}
