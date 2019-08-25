package com.github.promoapp.apresentacao.anuncio;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.github.promoapp.R;
import com.github.promoapp.dominio.anuncio.Anuncio;
import com.github.promoapp.dominio.anuncio.AnuncioRepository;

import java.util.Date;

public class AnuncioDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AnuncioRepository anuncioRepository = new AnuncioRepository(getApplicationContext());
                    Anuncio anuncio = new Anuncio();

                    EditText nomeAnuncioText = (EditText) findViewById(R.id.nomeText);
                    anuncio.setNome(nomeAnuncioText.getText().toString());

                    EditText nomeProdutoText = (EditText) findViewById(R.id.nomeProdutoText);
                    anuncio.setDescricao(nomeProdutoText.getText().toString());

                    EditText precoProdutoText = (EditText) findViewById(R.id.precoProdutoText);
                    double preco = Double.parseDouble(precoProdutoText.getText().toString());
                    anuncio.setPreco(preco);

                    EditText linkPrdotutoText = (EditText) findViewById(R.id.linkProdutoText);
                    anuncio.setUrl(linkPrdotutoText.getText().toString());

                    anuncio.setValidade(new Date());

                    anuncioRepository.salvar(anuncio);

                    Snackbar.make(view, "Anúncio salvo com sucesso", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } catch (Exception ex) {
                    Snackbar.make(view, "Ocorreu um erro ao tentar salvar o anúncio",
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();

            arguments.putString(AnuncioDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(AnuncioDetailFragment.ARG_ITEM_ID));

            AnuncioDetailFragment fragment = new AnuncioDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.anuncio_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this,
                    AnuncioListActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
