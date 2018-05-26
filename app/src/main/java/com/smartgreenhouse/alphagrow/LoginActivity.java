package com.smartgreenhouse.alphagrow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void efetuarLogin(View view){
        //Pegando os valores da view activity_login, atravas de seu ID
        email = (EditText) findViewById(R.id.editTextUserEmail);
        senha = (EditText) findViewById(R.id.editTextPassword);
    }

    private void validar(EditText email, EditText senha) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
