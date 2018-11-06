package com.smartgreenhouse.alphagrow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.smartgreenhouse.alphagrow.config.APIConfig;
import com.smartgreenhouse.alphagrow.models.Atuador;
import com.smartgreenhouse.alphagrow.services.AtuadorService;
import com.smartgreenhouse.alphagrow.services.AutenticacaoService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TemperaturaActivity extends AppCompatActivity {

    private String TOKEN;
    TextView tvTemperatura;
    ImageButton buttonVentilador1;
    ImageButton buttonVentilador2;
    AtuadorService atuadorService;

    Map<String, Atuador> estadosBotoes = new HashMap();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperatura);

        Intent intent = getIntent();
        String temperatura = intent.getStringExtra("temperatura");
        TOKEN = intent.getStringExtra("token");

        estadosBotoes.put("ventilador1", new Atuador("ventilador1", false));
        estadosBotoes.put("ventilador2", new Atuador("ventilador2", false));

        atuadorService = APIConfig.getClient().create(AtuadorService.class);

        tvTemperatura = (TextView) findViewById(R.id.textViewMetricaTemperatura);
        tvTemperatura.setText(temperatura);

        buttonVentilador1 = (ImageButton) findViewById(R.id.buttonVentilador1);
        buttonVentilador2 = (ImageButton) findViewById(R.id.buttonVentilador2);

        buttonVentilador1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterarEstadoBotão("ventilador1");
            }
        });

        buttonVentilador2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterarEstadoBotão("ventilador2");
            }
        });
    }

    private void alterarEstadoBotão(String index) {
        Atuador atuador = estadosBotoes.get(index);

        atuador.setEstado(!atuador.isEstado());


        Call<Atuador> call = atuadorService.salvarEstadoAtuador(atuador);
        call.enqueue(new Callback<Atuador>() {
            @Override
            public void onResponse(Call<Atuador> call, Response<Atuador> response) {
//                estadosBotoes.get(index).set
                Log.i("RESPOSTA", "Sistema mudou o estado do atuador ");
            }


            @Override
            public void onFailure(Call<Atuador> call, Throwable t) {

            }
        });
    }

    private void obterEstadoAtuadores(){
        Call<Atuador> call = atuadorService.obterEstadoAtuadores(TOKEN);
        call.enqueue(new Callback<Atuador>() {
            @Override
            public void onResponse(Call<Atuador> call, Response<Atuador> response) {
//                estadosBotoes.get(index).set

            }


            @Override
            public void onFailure(Call<Atuador> call, Throwable t) {

            }
        });
    }
}
