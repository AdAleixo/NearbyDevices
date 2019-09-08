package com.github.promoapp.apresentacao.anuncio;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.promoapp.R;
import com.github.promoapp.dominio.anuncio.Anuncio;
import com.github.promoapp.dominio.anuncio.AnuncioRepository;

public class AnuncioDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private Anuncio mItem;

    public AnuncioDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Long id = Long.parseLong(getArguments().getString(ARG_ITEM_ID));
            AnuncioRepository anuncioRepository = new AnuncioRepository(getContext());

            if (id != 0) {
                mItem = anuncioRepository.recuperarPorId(id);
            } else {
                mItem = new Anuncio();
            }

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);

            if (appBarLayout != null) {
//                appBarLayout.setTitle("Anúncio");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.anuncio_detail, container, false);

        if (mItem != null) {
            // ((TextView) rootView.findViewById(R.id.anuncio_detail)).setText(mItem.getDescricao());
        }

        return rootView;
    }
}
