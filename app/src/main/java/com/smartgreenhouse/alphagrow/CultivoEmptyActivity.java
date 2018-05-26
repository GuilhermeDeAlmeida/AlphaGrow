package com.smartgreenhouse.alphagrow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CultivoEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultivo_empty);
    }

    public void redirecionarParaCadastro(View view){
        Intent intent  = new Intent(CultivoEmptyActivity.this, CadastroCultivoActivity.class);
        startActivity(intent);
    }
}
