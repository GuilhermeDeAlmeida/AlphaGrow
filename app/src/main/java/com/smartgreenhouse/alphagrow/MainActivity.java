package com.smartgreenhouse.alphagrow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void redirecionarParaTemperatura(View view){
        Intent intent = new Intent(MainActivity.this, TemperaturaActivity.class);
        startActivity(intent);
    }

    public void redirecionarParaIndicePH(View view){
        Intent intent = new Intent(MainActivity.this, IndicePHActivity.class);
        Map yvesTest = new HashMap<String, String>();

        yvesTest.put("yves","Panaca");

        startActivity(intent);
    }

    public void redirecionarParaIluminacao(View view){
        Intent intent = new Intent(MainActivity.this, IluminacaoActivity.class);
        startActivity(intent);
    }
}
