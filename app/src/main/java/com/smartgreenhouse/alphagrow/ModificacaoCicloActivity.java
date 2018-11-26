package com.smartgreenhouse.alphagrow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smartgreenhouse.alphagrow.config.APIConfig;
import com.smartgreenhouse.alphagrow.models.Ciclo;
import com.smartgreenhouse.alphagrow.models.Cultivo;
import com.smartgreenhouse.alphagrow.models.Login;
import com.smartgreenhouse.alphagrow.services.CicloService;
import com.smartgreenhouse.alphagrow.services.CultivoService;
import com.smartgreenhouse.alphagrow.services.UsuarioService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModificacaoCicloActivity extends AppCompatActivity {


    private TextView textoNome;
    private TextView textoTemperatura;
    private TextView textoDuracao;
    private TextView textoUmidade;
    private String ID_LOGIN;
    private UsuarioService usuarioService;
    private CicloService cicloService;
    private Ciclo cicloAtual;
    private Button botaoSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificacao_ciclo);
        Intent intent = getIntent();
        ID_LOGIN = intent.getStringExtra("idLogin");
        usuarioService = APIConfig.getClient().create(UsuarioService.class);
        cicloService = APIConfig.getClient().create(CicloService.class);
        obterCultivo();
        inicializarCamposTexto();
        inicializarBotaoSalvar();
    }

    private void inicializarBotaoSalvar() {
        botaoSalvar = (Button) findViewById(R.id.botaoSalvar);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obterCicloDaView();
                salvarCicloAtual();
            }
        });
    }

    private void salvarCicloAtual() {
        Call<Ciclo> call = cicloService.salvarCiclo(cicloAtual);
        call.enqueue(new Callback<Ciclo>() {
            @Override
            public void onResponse(Call<Ciclo> call, Response<Ciclo> response) {
                exibirMensagemSucesso();
            }

            @Override
            public void onFailure(Call<Ciclo> call, Throwable t) {

            }
        });
    }

    private void exibirMensagemSucesso() {
        Toast.makeText(this, "Cultivo salvo", Toast.LENGTH_SHORT).show();
    }

    private void inicializarCamposTexto() {
        textoNome = (TextView)findViewById(R.id.textoNome);
        textoDuracao = (TextView)findViewById(R.id.textoDuracao);
        textoUmidade= (TextView)findViewById(R.id.textoUmidade);
        textoTemperatura= (TextView)findViewById(R.id.textoTemperatura);
    }

    private void obterCultivo(){
        Call<Login> call = usuarioService.obeterLogin(ID_LOGIN);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                List<Ciclo> ciclos = response.body().getUsuario().getRasp().getCultivo().getCiclos();
                cicloAtual = obterCicloAtual(ciclos);
                carregarTextViews(cicloAtual);
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });
    }

    private void carregarTextViews(Ciclo ciclo){
        textoDuracao.setText(String.valueOf(ciclo.getDuracao()));
        textoNome.setText(ciclo.getNome());
        textoUmidade.setText(ciclo.getControladoresIdeal().getUmidade());
        textoTemperatura.setText(ciclo.getControladoresIdeal().getTemperatura());
    }

    private Ciclo obterCicloAtual(List<Ciclo> ciclos){
        Ciclo cicloAtual = new Ciclo();

        for (Ciclo ciclo :
                ciclos) {
            if(ciclo.getCicloAtual()){
                cicloAtual = ciclo;
            }
        }

        return cicloAtual;
    }

    private String converterTextViewEmString(TextView textView){
        return textView.getText().toString();
    }

    private void obterCicloDaView(){
        cicloAtual.setNome(converterTextViewEmString(textoNome));
        cicloAtual.setDuracao(Integer.parseInt(converterTextViewEmString(textoDuracao)));
        cicloAtual.getControladoresIdeal().setTemperatura(converterTextViewEmString(textoTemperatura));
        cicloAtual.getControladoresIdeal().setUmidade(converterTextViewEmString(textoUmidade));

    }
}
