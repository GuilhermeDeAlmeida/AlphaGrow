package com.smartgreenhouse.alphagrow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.smartgreenhouse.alphagrow.config.APIConfig;
import com.smartgreenhouse.alphagrow.models.Controlador;
import com.smartgreenhouse.alphagrow.services.ControladorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "RESPOSTA>>>" ;
    ControladorService controladorService;
    ArrayList<Controlador> listaControladores = new ArrayList<>();
    TextView tvTemperatura;
    TextView tvUmidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controladorService = APIConfig.getClient().create(ControladorService.class);

        Call<ArrayList<Controlador>> call = controladorService.obterControladores();
        call.enqueue(new Callback<ArrayList<Controlador>>() {
            @Override
            public void onResponse(Call<ArrayList<Controlador>> call, Response<ArrayList<Controlador>> response) {
                Log.i(TAG, listaControladores.toString());

                popularControladores(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Controlador>> call, Throwable t) {

            }
        });
    }

    private void popularControladores(ArrayList<Controlador> listaControladores) {
        this.listaControladores = listaControladores;

        this.tvTemperatura = (TextView) findViewById(R.id.textViewTemperatura);
        this.tvUmidade = (TextView) findViewById(R.id.textViewUmidade);

        for (Controlador controlador:listaControladores) {
            if(controlador.getNomeControlador().toUpperCase().equals("UMIDADE")){
                tvUmidade.setText(controlador.getValorControlador());
            }else{
                tvTemperatura.setText(controlador.getValorControlador());
            }
        }

    }

    public void redirecionarParaTemperatura(View view){
        Intent intent = new Intent(MainActivity.this, TemperaturaActivity.class);
        startActivity(intent);
    }

    public void redirecionarParaIndicePH(View view){
        Intent intent = new Intent(MainActivity.this, IndicePHActivity.class);
        startActivity(intent);
    }

    public void redirecionarParaIluminacao(View view){
        Intent intent = new Intent(MainActivity.this, IluminacaoActivity.class);
        startActivity(intent);
    }
}
