package com.github.promoapp.apresentacao.anuncio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.promoapp.R;

public class TelaCriaAnuncio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criaranuncio);
        Button salvarButton = (Button) findViewById(R.id.salvarButton);

        salvarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSalvarAnuncio();
            }
        });
    }

    public void onSalvarAnuncio() {
        EditText nomeAnuncioText = (EditText) findViewById(R.id.nomeText);

    }
}
