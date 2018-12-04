package com.smartgreenhouse.alphagrow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.smartgreenhouse.alphagrow.config.APIConfig;
import com.smartgreenhouse.alphagrow.models.Atuador;
import com.smartgreenhouse.alphagrow.services.AtuadorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TemperaturaActivity extends AppCompatActivity {

    private static final String TAG = "RESPOSTA>>>>>";
    TextView tvTemperatura;
    ImageButton buttonVentilador;
    AtuadorService atuadorService;
    String ID_TOKEN;
    private Handler handler;

    Map<String, Atuador> estadosBotoes = new HashMap();
    private Atuador atuadorVentilador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperatura);

        Intent intent = getIntent();
        String temperatura = intent.getStringExtra("temperatura");

        atuadorService = APIConfig.getClient().create(AtuadorService.class);
        ID_TOKEN = intent.getStringExtra("token");

        tvTemperatura = (TextView) findViewById(R.id.textViewMetricaTemperatura);
        tvTemperatura.setText(temperatura);


        inicializarBotoes();
        carregarDadosAtuadores();
        manterDadosAtuadores();

    }

    private void manterDadosAtuadores() {
        handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                carregarDadosAtuadores();
                handler.postDelayed(this, 100);
            }
        };
        handler.postDelayed(r, 100);
    }

    private void carregarDadosAtuadores() {
        Call<List<Atuador>> call = atuadorService.obterEstadoAtuadores(ID_TOKEN);
        call.enqueue(new Callback<List<Atuador>>() {
            @Override
            public void onResponse(Call<List<Atuador>> call, Response <List<Atuador>> response) {
                atuadorVentilador = obterAtuadorVentilador(response.body());
                buttonVentilador.setBackgroundColor(obterCor(atuadorVentilador.isEstado()));
            }

            @Override
            public void onFailure(Call<List<Atuador>> call, Throwable t) {
                Log.w(TAG, "Erro ao obter login");
            }
        });
    }

    private Atuador obterAtuadorVentilador(List<Atuador> atuadores){
        for (Atuador atuador : atuadores) {
            if (atuador.getAtuador().toLowerCase().equals("ventilador")){
                return atuador;
            }
        }
        return new Atuador();
    }

    private void inicializarBotoes() {
        buttonVentilador = (ImageButton) findViewById(R.id.buttonVentilador1);
        buttonVentilador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atuadorVentilador.setEstado(!atuadorVentilador.isEstado());
                salvarAtuador();
            }
        });


    }

    private void salvarAtuador() {
        Call<Boolean> call = atuadorService.salvarEstadoAtuador(atuadorVentilador);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.i(TAG, "Mudou o estado do atuador");
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.w(TAG, "Não conseguiu alterar o estado do atuador");
            }
        });
    }

    private void alterarEstadoBotão(String index) {
        Atuador atuador = estadosBotoes.get(index);

        atuador.setEstado(!atuador.isEstado());


        Call<Boolean> call = atuadorService.salvarEstadoAtuador(atuador);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//                estadosBotoes.get(index).set
                Log.i("RESPOSTA", "Sistema mudou o estado do atuador ");
            }


            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    private int obterCor(Boolean ativo){
        return ativo? Color.rgb(0, 255, 0) : Color.rgb(255, 0, 0);
        //
    }

//    private void obterEstadoAtuadores(){
//        Call<List<Atuador>> call = atuadorService.obterEstadoAtuadores(TOKEN);
//        call.enqueue(new Callback<Atuador>() {
//            @Override
//            public void onResponse(Call<Atuador> call, Response<Atuador> response) {
////                estadosBotoes.get(index).set
//
//            }
//
//
//            @Override
//            public void onFailure(Call<Atuador> call, Throwable t) {
//
//            }
//        });
//    }
}
