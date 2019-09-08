package com.github.promoapp.apresentacao.anuncio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.promoapp.R;
import com.github.promoapp.dominio.anuncio.Anuncio;
import com.github.promoapp.dominio.anuncio.AnuncioRepository;

import java.util.Date;
import java.util.List;

public class AnuncioListActivity extends AppCompatActivity {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(AnuncioDetailFragment.ARG_ITEM_ID, "0");
                        AnuncioDetailFragment fragment = new AnuncioDetailFragment();
                        fragment.setArguments(arguments);

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.anuncio_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = findViewById(R.id.anuncio_list).getContext();
                        Intent intent = new Intent(context, AnuncioDetailActivity.class);
                        intent.putExtra(AnuncioDetailFragment.ARG_ITEM_ID, "0");

                        context.startActivity(intent);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();

                    Snackbar.make(findViewById(R.id.anuncio_list), "Erro: " + ex.getMessage(),
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        if (findViewById(R.id.anuncio_detail_container) != null) {
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.anuncio_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        try {
            AnuncioRepository anuncioRepository = new AnuncioRepository(getApplicationContext());
            /*
            Anuncio anuncio = new Anuncio();
            anuncio.setNome("Nome 2");
            anuncio.setDescricao("Descrição 2");
            anuncio.setValidade(new Date());
            anuncio.setUrl("http://facebook.com");
            anuncio.setPreco(12.99);
            anuncioRepository.salvar(anuncio);
            */
            List<Anuncio> anuncios = anuncioRepository.recuperarTodos();

            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, anuncios,
                    mTwoPane));
        } catch (Exception ex) {
            ex.printStackTrace();

            Snackbar.make(recyclerView, "Erro: " + ex.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final AnuncioListActivity mParentActivity;
        private final List<Anuncio> mValues;
        private final boolean mTwoPane;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Anuncio item = (Anuncio) view.getTag();

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(AnuncioDetailFragment.ARG_ITEM_ID, item.getId().toString());
                    AnuncioDetailFragment fragment = new AnuncioDetailFragment();
                    fragment.setArguments(arguments);

                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.anuncio_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, AnuncioDetailActivity.class);
                    intent.putExtra(AnuncioDetailFragment.ARG_ITEM_ID, item.getId().toString());

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(AnuncioListActivity parent,
                                      List<Anuncio> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.anuncio_list_content, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getId().toString());
            holder.mContentView.setText(mValues.get(position).getNome());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
