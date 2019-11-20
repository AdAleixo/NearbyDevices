package com.github.promoapp.apresentacao.promocao;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.promoapp.R;
import com.github.promoapp.dominio.anuncio.Anuncio;
import com.github.promoapp.dominio.anuncio.AnuncioMessage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TelaOferta extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private boolean mTwoPane;
    private static final int TTL_IN_SECONDS = 3 * 60; // Three minutes.
    private GoogleApiClient mGoogleApiClient;
    private static final String KEY_UUID = "key_uuid";

    private Message mPubMessage;
    private MessageListener mMessageListener;
    private List<Anuncio> mAnuncios = new ArrayList<>();

    private static final Strategy PUB_SUB_STRATEGY = (new Strategy.Builder()).zze(2)
            .setTtlSeconds(TTL_IN_SECONDS).build();

    private static String getUUID(SharedPreferences sharedPreferences) {
        String uuid = sharedPreferences.getString(KEY_UUID, "");
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            sharedPreferences.edit().putString(KEY_UUID, uuid).apply();
        }
        return uuid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promocao_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.promocao_detail_container) != null) {
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.promocao_list);
        setupRecyclerView((RecyclerView) recyclerView);

        mMessageListener = new MessageListener() {
            @Override
            public void onFound(final Message message) {
                Anuncio anuncio = AnuncioMessage.fromNearbyMessage(message).getAnuncio();
                mAnuncios.add(anuncio);

                View recyclerView = findViewById(R.id.promocao_list);
                setupRecyclerView((RecyclerView) recyclerView);
            }

            @Override
            public void onLost(final Message message) {
                mAnuncios.remove(AnuncioMessage.fromNearbyMessage(message).getAnuncio());
                View recyclerView = findViewById(R.id.promocao_list);
                setupRecyclerView((RecyclerView) recyclerView);
            }
        };

        buildGoogleApiClient();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            subscribe();
        }
    }



    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this,
                mAnuncios, mTwoPane));
    }

    private void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return;
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        logAndShowSnackbar("Exception while connecting to Google Play services: " +
                connectionResult.getErrorMessage());
    }

    @Override
    public void onConnectionSuspended(int i) {
        logAndShowSnackbar("Connection suspended. Error code: " + i);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        subscribe();
    }

    /**
     * Subscribes to messages from nearby devices and updates the UI if the subscription either
     * fails or TTLs.
     */
    private void subscribe() {
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(PUB_SUB_STRATEGY)
                .setCallback(new SubscribeCallback() {
                    @Override
                    public void onExpired() {
                        super.onExpired();
                    }
                }).build();

        Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener, options)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (!status.isSuccess()) {
                            logAndShowSnackbar("Could not subscribe, status = " + status);
                        }
                    }
                });
    }

    /**
     * Stops subscribing to messages from nearby devices.
     */
    //private void unsubscribe() {
      //  Nearby.Messages.unsubscribe(mGoogleApiClient, mMessageListener);
    //}

    private void logAndShowSnackbar(final String text) {
        View container = findViewById(R.id.activity_main_container);

        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final TelaOferta mParentActivity;
        private final List<Anuncio> mValues;
        private final boolean mTwoPane;

        SimpleItemRecyclerViewAdapter(TelaOferta parent, List<Anuncio> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.promocao_list_content, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Anuncio anuncio = mValues.get(position);
            holder.mAnuncioLinkView.setText(anuncio.getUrl());
            holder.mContentView.setText(anuncio.getNome());
            holder.itemView.setTag(mValues.get(position));
            //holder.mAnuncioPreco.setText(anuncio.getPreco().toString());

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final TextView mContentView;
            final TextView mAnuncioLinkView;
           // final TextView mAnuncioPreco;

            ViewHolder(View view) {
                super(view);
                mAnuncioLinkView = view.findViewById(R.id.anuncio_url);
                mContentView = view.findViewById(R.id.content);
              //  mAnuncioPreco = view.findViewById(R.id.precoProdutoText);
            }
        }
    }
}
