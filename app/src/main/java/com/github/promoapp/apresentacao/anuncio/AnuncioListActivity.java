package com.github.promoapp.apresentacao.anuncio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.github.promoapp.R;
import com.github.promoapp.apresentacao.MainActivity;
import com.github.promoapp.dominio.anuncio.Anuncio;
import com.github.promoapp.dominio.anuncio.AnuncioMessage;
import com.github.promoapp.dominio.anuncio.AnuncioRepository;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.Strategy;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AnuncioListActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private boolean mTwoPane;
    GoogleApiClient mGoogleApiClient;

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
        buildGoogleApiClient();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(getClass().getSimpleName(), "GoogleApiClient connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        logAndShowSnackbar("Connection suspended. Error code: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        logAndShowSnackbar("Exception while connecting to Google Play services: " +
                connectionResult.getErrorMessage());
    }

    private void logAndShowSnackbar(final String text) {
        Log.w(getClass().getSimpleName(), text);
        View container = findViewById(R.id.activity_main_container);

        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return;
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API, new MessagesOptions.Builder()
                        .setPermissions(NearbyPermissions.BLE).build())
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .build();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        try {
            AnuncioRepository anuncioRepository = new AnuncioRepository(getApplicationContext());

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
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.AnuncioViewHolder> {

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
        public AnuncioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.anuncio_list_content, parent, false);

            return new AnuncioViewHolder(view, mParentActivity,
                    mParentActivity.mGoogleApiClient);
        }

        @Override
        public void onBindViewHolder(final AnuncioViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getId().toString());
            holder.mContentView.setText(mValues.get(position).getNome());
            holder.mAnuncio = mValues.get(position);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class AnuncioViewHolder extends RecyclerView.ViewHolder {

            private static final String KEY_UUID = "key_uuid";
            private static final int TTL_IN_SECONDS = 3 * 60; // Three minutes.

            private final Strategy PUB_SUB_STRATEGY = (new Strategy.Builder()).zze(2)
                    .setTtlSeconds(TTL_IN_SECONDS).build();

            TextView mIdView;
            TextView mContentView;
            Switch mPublishSwitch;

            Button mDeleteButton;

            GoogleApiClient mGoogleApiClient;
            View mView;
            Anuncio mAnuncio;
            AnuncioListActivity mActivity;
            Message mPubMessage;

            AnuncioViewHolder(View view, AnuncioListActivity activity,
                              GoogleApiClient googleApiClient) {

                super(view);

                mView = view;
                mActivity = activity;
                mGoogleApiClient = googleApiClient;
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
                mPublishSwitch = view.findViewById(R.id.publishSwitch);

                mDeleteButton = view.findViewById(R.id.deleteButton);

                mDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            AnuncioRepository anuncioRepository = new AnuncioRepository(
                                    mActivity.getApplicationContext());

                            Anuncio anuncio = new Anuncio();

                            anuncioRepository.deletar(anuncio);

                            Snackbar.make(view, "Anúncio deletado com sucesso", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } catch (Exception ex) {
                            Snackbar.make(view, "Ocorreu um erro ao tentar deletar o anúncio",
                                    Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            Log.i(getClass().getSimpleName(),ex.getMessage());
                            ex.printStackTrace();

                        }
                    }
                });




                mPublishSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                            if (isChecked) {
                                publish();
                            } else {
                                unpublish();
                            }
                        }
                    }
                });
            }

            private void publish() {
                mPubMessage = AnuncioMessage.newNearbyMessage(getUUID(mActivity.getSharedPreferences(
                                mActivity.getApplicationContext().getPackageName(),
                                Context.MODE_PRIVATE)), mAnuncio);

                Log.i(getClass().getSimpleName(), "Publishing");

                PublishOptions options = new PublishOptions.Builder()
                        .setStrategy(PUB_SUB_STRATEGY)
                        .setCallback(new PublishCallback() {
                            @Override
                            public void onExpired() {
                                super.onExpired();
                                Log.i(getClass().getSimpleName(), "No longer publishing");

                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPublishSwitch.setChecked(false);
                                    }
                                });
                            }
                        }).build();

                Nearby.Messages.publish(mGoogleApiClient, mPubMessage, options)
                        .setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (status.isSuccess()) {
                                    Log.i(getClass().getSimpleName(), "Published successfully.");
                                } else {
                                    logAndShowSnackbar("Could not publish, status = " + status);
                                    mPublishSwitch.setChecked(false);
                                }
                            }
                        });
            }

            private String getUUID(SharedPreferences sharedPreferences) {
                String uuid = sharedPreferences.getString(KEY_UUID, "");

                if (TextUtils.isEmpty(uuid)) {
                    uuid = UUID.randomUUID().toString();
                    sharedPreferences.edit().putString(KEY_UUID, uuid).apply();
                }

                return uuid;
            }

            private void unpublish() {
                Log.i(getClass().getSimpleName(), "Unpublishing.");
                Nearby.Messages.unpublish(mGoogleApiClient, mPubMessage);
            }

            private void logAndShowSnackbar(final String text) {
                Log.w(getClass().getSimpleName(), text);
                View container = mView.findViewById(R.id.activity_main_container);

                if (container != null) {
                    Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }
}
