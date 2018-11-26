package com.smartgreenhouse.alphagrow;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smartgreenhouse.alphagrow.config.APIConfig;
import com.smartgreenhouse.alphagrow.models.Ciclo;
import com.smartgreenhouse.alphagrow.models.Controlador;
import com.smartgreenhouse.alphagrow.models.Cultivo;
import com.smartgreenhouse.alphagrow.models.Login;
import com.smartgreenhouse.alphagrow.services.ControladorService;
import com.smartgreenhouse.alphagrow.services.CultivoService;
import com.smartgreenhouse.alphagrow.services.UsuarioService;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RESPOSTA>>>" ;
    private ControladorService controladorService;
    private CultivoService cultivoService;
    private UsuarioService usuarioService;

    private ArrayList<Controlador> listaControladores = new ArrayList<>();
    private TextView tvTemperatura;
    private TextView tvUmidade;
    private TextView tvTemperaturaAdequada;
    private TextView tvUmidadeAdequada;
    private TextView tvDiasProxCiclo;
    private TextView tvInformacoesCiclo;
    private TextView tvUsuario;

    private Handler handler;
    private String umidadeAtual;
    private String temperaturaAtual;

    private String ID_LOGIN;
    private Button botaoModificarCultivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvUsuario = (TextView) findViewById(R.id.textViewUsuario);
        controladorService = APIConfig.getClient().create(ControladorService.class);
        cultivoService = APIConfig.getClient().create(CultivoService.class);
        usuarioService = APIConfig.getClient().create(UsuarioService.class);
        ID_LOGIN = getIntent().getStringExtra("idLogin");

        inicializarBotaoModificarCultivo();


        carregarDadosCiclo();
        manterDadosCiclo();

    }

    private void inicializarBotaoModificarCultivo() {

        botaoModificarCultivo = (Button) findViewById(R.id.botaoModificarCultivo);
        botaoModificarCultivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirecionarParaModificarCultivo();
            }
        });
    }

    private void manterDadosCiclo() {
        handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                carregarDadosCiclo();
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(r, 5000);
    }

    private void carregarDadosCiclo() {

        Log.i(TAG, "Consultando através do usuarioService.obeterLogin(" + ID_LOGIN +")");
        Call<Login> call = usuarioService.obeterLogin(ID_LOGIN);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                tvUsuario.setText( "Bem vindo " + response.body().getUsuario().getNome());
                Log.i(TAG, listaControladores.toString());
                Cultivo cultivo= response.body().getUsuario().getRasp().getCultivo();
                for (Ciclo ciclo : cultivo.getCiclos()) {
                    if(ciclo.getCicloAtual()){
                        popularTela(cultivo, ciclo);
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });
//        Log.i(TAG, "Consultando através controladorService.obterCultivo(" +ID_CULTIVO +")");
//        Call<Cultivo> call = controladorService.obterCultivo(ID_CULTIVO );
//        call.enqueue(new Callback<Cultivo>() {
//            @Override
//            public void onResponse(Call<Cultivo> call, Response<Cultivo> response) {
//                Log.i(TAG, listaControladores.toString());
////                Toast.makeText()
//
//                for (iclo ciclo : response.body().getCiclos()) {
//                    if(ciclo.getCicloAtual()){
//                        popularTela(response.body(), ciclo);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Cultivo> call, Throwable t) {
//                Log.e(TAG, t.getMessage());
//            }
//        });
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
                umidadeAtual = controlador.getValorControlador();
            }else{
                tvTemperatura.setText(controlador.getValorControlador() + "º");
                temperaturaAtual = controlador.getValorControlador();
            }
        }
        tvTemperaturaAdequada.setText(ciclo.getControladoresIdeal().getTemperatura() + "º");
        tvUmidadeAdequada.setText(ciclo.getControladoresIdeal().getUmidade() + "%");
    }

    public void redirecionarParaTemperatura(View view){
        Intent intent = new Intent(MainActivity.this, TemperaturaActivity.class);
        intent.putExtra("temperatura", temperaturaAtual);
        startActivity(intent);
    }

    public void redirecionarParaUmidade(View view){
        Intent intent = new Intent(MainActivity.this, UmidadeActivity.class);
        intent.putExtra("umidade", umidadeAtual);
        startActivity(intent);
    }

    public void redirecionarParaModificarCultivo(){
        Intent intent = new Intent(MainActivity.this, ModificacaoCicloActivity.class);
        intent.putExtra("idLogin", ID_LOGIN);
        startActivity(intent);
    }

    public void redirecionarParaIluminacao(View view){
        Intent intent = new Intent(MainActivity.this, IluminacaoActivity.class);
        startActivity(intent);
    }
}