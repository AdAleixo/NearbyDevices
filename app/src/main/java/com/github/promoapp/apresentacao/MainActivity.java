package com.github.promoapp.apresentacao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.promoapp.R;
import com.github.promoapp.apresentacao.anuncio.AnuncioListActivity;
import com.github.promoapp.apresentacao.promocao.TelaOferta;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void exibirAnuncioListActivity(View view) {
        Intent intent = new Intent(MainActivity.this, AnuncioListActivity.class);
        startActivity(intent);
    }



    //Button Ofertas
    public void openTelaOfertas(View view) {
        Intent intent = new Intent(MainActivity.this, TelaOferta.class);
        startActivity(intent);
    }

   

}