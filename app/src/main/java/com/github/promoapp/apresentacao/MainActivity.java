package com.github.promoapp.apresentacao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.promoapp.R;
import com.github.promoapp.apresentacao.anuncio.TelaAnuncio;
import com.github.promoapp.apresentacao.anuncio.TelaCriaAnuncio;
import com.github.promoapp.apresentacao.promocao.TelaOferta;


public class MainActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    //Button Anunciar
    public void openTelaAnuncio(View view) {
        Intent intent = new Intent(MainActivity.this, TelaAnuncio.class);
        startActivity(intent);
    }

    //Button Ofertas
    public void openTelaOfertas(View view) {
        Intent intent = new Intent(MainActivity.this, TelaOferta.class);
        startActivity(intent);
    }

    //Button Criar Ofertas
    public void openTelaCriaAnuncio(View view) {
        Intent intent = new Intent(MainActivity.this, TelaCriaAnuncio.class);
        startActivity(intent);
    }

}