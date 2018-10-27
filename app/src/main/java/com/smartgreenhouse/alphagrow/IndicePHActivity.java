package com.smartgreenhouse.alphagrow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class IndicePHActivity extends AppCompatActivity {

    TextView tvUmidade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indice_ph);
        Intent intent = getIntent();
        String umidade = intent.getStringExtra("umidade");
        tvUmidade = (TextView) findViewById(R.id.textViewMetricaUmidade);
        tvUmidade.setText(umidade);
    }
}