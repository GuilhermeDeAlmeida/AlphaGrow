package com.smartgreenhouse.alphagrow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TemperaturaActivity extends AppCompatActivity {

    TextView tvTemperatura;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperatura);

        Intent intent = getIntent();
        String temperatura = intent.getStringExtra("temperatura");
        tvTemperatura = (TextView) findViewById(R.id.textViewMetricaTemperatura);
        tvTemperatura.setText(temperatura);
    }
}
