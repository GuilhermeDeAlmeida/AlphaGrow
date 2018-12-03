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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UmidadeActivity extends AppCompatActivity {

    private static final String TAG = "RESPOSTA>>>>";
    private AtuadorService atuadorService;
    private Handler handler;
    TextView tvUmidade;
    ImageButton botaoBomba;
    List<Atuador> atuadores = new ArrayList<Atuador>();
    Atuador atuadorBombaDagua = new Atuador();
    String ID_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umidade);
        Intent intent = getIntent();
        String umidade = intent.getStringExtra("umidade");
        ID_TOKEN = intent.getStringExtra("token");

        atuadorService= APIConfig.getClient().create(AtuadorService.class);

        tvUmidade = (TextView) findViewById(R.id.textViewMetricaUmidade);
        tvUmidade.setText(umidade);

        inicializarBotao();
        carregarDadosAtuadores();
        manterDadosAtuadores();

//        botaoBomba.setBackground();
    }

    private void inicializarBotao() {
        botaoBomba = (ImageButton)findViewById(R.id.buttonBomba);
        botaoBomba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atuadorBombaDagua.setEstado(!atuadorBombaDagua.isEstado());
                salvarAtuador();
            }
        });
    }

    private void salvarAtuador() {
        Call<Boolean> call = atuadorService.salvarEstadoAtuador(atuadorBombaDagua);
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

    private void carregarDadosAtuadores() {
        Call<List<Atuador>> call = atuadorService.obterEstadoAtuadores(ID_TOKEN);
        call.enqueue(new Callback<List<Atuador>>() {
            @Override
            public void onResponse(Call<List<Atuador>> call, Response <List<Atuador>> response) {
                atuadorBombaDagua = obterAtuadorBombaDagua(response.body());
                botaoBomba.setBackgroundColor(obterCor(atuadorBombaDagua.isEstado()));
            }

            @Override
            public void onFailure(Call<List<Atuador>> call, Throwable t) {
                Log.w(TAG, "Erro ao obter login");
            }
        });
    }

    private void manterDadosAtuadores() {
        handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                carregarDadosAtuadores();
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(r, 5000);
    }

    private int obterCor(Boolean ativo){
        return ativo? Color.rgb(0, 255, 0) : Color.rgb(255, 0, 0);
        //
    }

    private Atuador obterAtuadorBombaDagua(List<Atuador> atuadores){
        for (Atuador atuador :
                atuadores) {
            if (atuador.getAtuador().toLowerCase().equals("bomba-dagua")){
                return atuador;
            }
        }
        return new Atuador();
    }
}