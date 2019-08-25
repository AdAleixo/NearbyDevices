package com.github.promoapp.apresentacao.anuncio;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.EditText;

import com.github.promoapp.R;
import com.github.promoapp.dominio.anuncio.Anuncio;
import com.github.promoapp.dominio.anuncio.AnuncioRepository;

import java.util.Date;

/**
 * An activity representing a single Anuncio detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link AnuncioListActivity}.
 */
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
                }catch (Exception ex){
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

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to  landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
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
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, AnuncioListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
