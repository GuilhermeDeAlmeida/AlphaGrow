package com.smartgreenhouse.alphagrow;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
    private String TOKEN;

    private Button botaoModificarCultivo;
    private ProgressBar progressBar;
    private Ciclo cicloAtual = new Ciclo();
    private long diasCorridosCicloAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvUsuario = (TextView) findViewById(R.id.textViewUsuario);




        incializarServicos();
        ID_LOGIN = getIntent().getStringExtra("idLogin");
        TOKEN = getIntent().getStringExtra("token");
        inicializarBotaoModificarCultivo();
        carregarDadosCiclo();
        manterDadosCiclo();

    }

    private void inicianilarProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(cicloAtual.getDuracao());
        diasCorridosCicloAtual = getDateDiff( cicloAtual.getDataInicio(), new Date(), TimeUnit.DAYS);
//        progressBar.setProgress((int)diasCorridosCicloAtual < cicloAtual.getDuracao() ? (int)diasCorridosCicloAtual : cicloAtual.getDuracao());
        progressBar.setProgress((int)diasCorridosCicloAtual);
    }

    private void incializarServicos() {
        controladorService = APIConfig.getClient().create(ControladorService.class);
        cultivoService = APIConfig.getClient().create(CultivoService.class);
        usuarioService = APIConfig.getClient().create(UsuarioService.class);
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
                handler.postDelayed(this, 500);
            }
        };
        handler.postDelayed(r, 500);
    }

    private void carregarDadosCiclo() {

        Log.i(TAG, "Consultando através do usuarioService.obeterLogin(" + ID_LOGIN +")");
        Call<Login> call = usuarioService.obterLogin(ID_LOGIN);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                tvUsuario.setText( "Bem vindo " + response.body().getUsuario().getNome());
                Log.i(TAG, listaControladores.toString());
                Cultivo cultivo= response.body().getUsuario().getRasp().getCultivo();

                for (Ciclo ciclo : cultivo.getCiclos()) {
                    if(ciclo.getCicloAtual()){
                       cicloAtual = ciclo;
                        inicianilarProgressBar();
                       popularTela(cultivo, ciclo);
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.w(TAG, "Erro ao obter login");
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

        long diferencaDias = diasCorridosCicloAtual - cicloAtual.getDuracao();
        if(diferencaDias <= 0 ){
            sb.append("Em ");
            sb.append(Long.toString(diferencaDias));
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
        intent.putExtra("token", TOKEN);
        startActivity(intent);
    }

    public void redirecionarParaUmidade(View view){
        Intent intent = new Intent(MainActivity.this, UmidadeActivity.class);
        intent.putExtra("umidade", umidadeAtual);
        intent.putExtra("token", TOKEN);
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