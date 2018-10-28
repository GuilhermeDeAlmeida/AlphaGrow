package com.smartgreenhouse.alphagrow;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.smartgreenhouse.alphagrow.config.APIConfig;
import com.smartgreenhouse.alphagrow.models.Ciclo;
import com.smartgreenhouse.alphagrow.models.Controlador;
import com.smartgreenhouse.alphagrow.models.Cultivo;
import com.smartgreenhouse.alphagrow.services.ControladorService;
import com.smartgreenhouse.alphagrow.services.CultivoService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "RESPOSTA>>>" ;
    ControladorService controladorService;
    CultivoService cultivoService;
    ArrayList<Controlador> listaControladores = new ArrayList<>();
    TextView tvTemperatura;
    TextView tvUmidade;
    TextView tvTemperaturaAdequada;
    TextView tvUmidadeAdequada;
    TextView tvDiasProxCiclo;
    TextView tvInformacoesCiclo;
    Handler handler;

    final String ID_CULTIVO = "5bcff24a949eee25408ad8b5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controladorService = APIConfig.getClient().create(ControladorService.class);
        cultivoService = APIConfig.getClient().create(CultivoService.class);
        carregarDadosCiclo();
        manterDadosCiclo();

    }

    //Será o metodo respons

    private void manterDadosCiclo() {
        handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                Log.i("RUNNABLE","Hello World");
                carregarDadosCiclo();
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(r, 5000);
    }

    private void carregarDadosCiclo() {
        Call<Cultivo> call = controladorService.obterCultivo(ID_CULTIVO );
        call.enqueue(new Callback<Cultivo>() {
            @Override
            public void onResponse(Call<Cultivo> call, Response<Cultivo> response) {
                Log.i(TAG, listaControladores.toString());
//                Toast.makeText()

                for (Ciclo ciclo : response.body().getCiclos()) {
                    if(ciclo.getCicloAtual()){
                        popularTela(response.body(), ciclo);
                    }
                }
            }

            @Override
            public void onFailure(Call<Cultivo> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void popularTela(Cultivo cultivo, Ciclo ciclo) {
        popularControladores(ciclo);
        popularCiclo(cultivo, ciclo);
    }

    private void popularCiclo(Cultivo cultivo, Ciclo ciclo) {
        popularInformacoesCiclo(cultivo, ciclo);
        popularDiasCiclos(ciclo);
    }

    private void popularDiasCiclos(Ciclo ciclo) {
        this.tvDiasProxCiclo = (TextView) findViewById(R.id.textViewDiasCiclo);
        StringBuilder sb = new StringBuilder();

        long days = getDateDiff(new Date() , ciclo.getDataFim() , TimeUnit.DAYS);
        if(days > 0){
            sb.append("Em ");
            sb.append(Long.toString(days));
            sb.append(" dias ele estará pronto para o proximo ciclo");
        }else{
            sb.append("O tempo sugerido para que seu cultivo avance de ciclo ja passou.");
        }
        tvDiasProxCiclo.setText(sb.toString());
    }

    private void popularInformacoesCiclo(Cultivo cultivo, Ciclo ciclo) {
        this.tvInformacoesCiclo = (TextView) findViewById(R.id.textViewInformacoesCiclo);
        StringBuilder sb = new StringBuilder();
        sb.append("Seu(ua) ");
        sb.append(cultivo.getNomeCultivo());
        sb.append(" está no ciclo ");
        sb.append(ciclo.getNome());


        tvInformacoesCiclo.setText(sb.toString());
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    private void popularControladores(Ciclo ciclo) {
        this.listaControladores = ciclo.getControladoresAtual();
        this.tvTemperatura = (TextView) findViewById(R.id.textViewTemperatura);
        this.tvUmidade = (TextView) findViewById(R.id.textViewUmidade);
        this.tvTemperaturaAdequada = (TextView) findViewById(R.id.textViewTemperaturaAdequada);
        this.tvUmidadeAdequada = (TextView) findViewById(R.id.textViewUmidadeAdequada);
        for (Controlador controlador : listaControladores) {
            if(controlador.getNomeControlador().toUpperCase().equals("UMIDADE")){
                tvUmidade.setText(controlador.getValorControlador() + "%");
            }else{
                tvTemperatura.setText(controlador.getValorControlador() + "º");
            }
        }
        tvTemperaturaAdequada.setText(ciclo.getControladoresIdeal().getTemperatura() + "º");
        tvUmidadeAdequada.setText(ciclo.getControladoresIdeal().getUmidade() + "%");
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