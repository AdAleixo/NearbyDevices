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


/**
 * A fragment representing a single Anuncio detail screen.
 * This fragment is either contained in a {@link AnuncioListActivity}
 * in two-pane mode (on tablets) or a {@link AnuncioDetailActivity}
 * on handsets.
 */
public class AnuncioDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Anuncio mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AnuncioDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Long id = Long.parseLong(getArguments().getString(ARG_ITEM_ID));
            AnuncioRepository anuncioRepository = new AnuncioRepository(getContext());

            if (id != 0) {
                mItem = anuncioRepository.recuperarPorId(id);
            } else {
                mItem = new Anuncio();
            }

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getNome());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.anuncio_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.anuncio_detail)).setText(mItem.getDescricao());
        }

        return rootView;
    }
}
