package br.com.ifoodclone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import br.com.ifoodclone.R;

public class SplachActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                abrirTelaAutenticacao();
            }
        },3000);
    }

    public void abrirTelaAutenticacao(){
        Intent i = new Intent(SplachActivity.this,AutenticacaoActivity.class);
        startActivity(i);
        finish();
    }
}