package com.google.android.gms.nearby.messages.samples.nearbydevices;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.SwitchCompat;
        import android.text.TextUtils;
        import android.util.Log;
        import android.widget.CompoundButton;

        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.ResultCallback;
        import com.google.android.gms.common.api.Status;
        import com.google.android.gms.nearby.Nearby;
        import com.google.android.gms.nearby.messages.Message;
        import com.google.android.gms.nearby.messages.MessageListener;
        import com.google.android.gms.nearby.messages.PublishCallback;
        import com.google.android.gms.nearby.messages.PublishOptions;
        import com.google.android.gms.nearby.messages.Strategy;
        import android.content.Context;
        import android.content.SharedPreferences;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.design.widget.Snackbar;
        import android.support.v4.app.FragmentActivity;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.SwitchCompat;
        import android.text.TextUtils;

        import android.util.Log;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.CompoundButton;
        import android.widget.ListView;

        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.ResultCallback;
        import com.google.android.gms.common.api.Status;
        import com.google.android.gms.nearby.Nearby;
        import com.google.android.gms.nearby.messages.Message;
        import com.google.android.gms.nearby.messages.MessageListener;
        import com.google.android.gms.nearby.messages.Messages;
        import com.google.android.gms.nearby.messages.PublishCallback;
        import com.google.android.gms.nearby.messages.PublishOptions;
        import com.google.android.gms.nearby.messages.Strategy;
        import com.google.android.gms.nearby.messages.SubscribeCallback;
        import com.google.android.gms.nearby.messages.SubscribeOptions;

        import java.util.ArrayList;
        import java.util.List;

        import java.util.UUID;
        import android.content.Intent;
        import android.os.Handler;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import java.util.UUID;

public class TelaAnuncio extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,

        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = TelaAnuncio.class.getSimpleName();

    private static final int TTL_IN_SECONDS = 3 * 60; // Three minutes.

    // Key used in writing to and reading from SharedPreferences.
    private static final String KEY_UUID = "key_uuid";

    /**
     * Sets the time in seconds for a published message or a subscription to live. Set to three
     * minutes.
     */
    private static final Strategy PUB_SUB_STRATEGY = (new Strategy.Builder()).zzjS(2)
            .setTtlSeconds(TTL_IN_SECONDS).build();

    /**
     * Creates a UUID and saves it to {@link SharedPreferences}. The UUID is added to the published
     * message to avoid it being undelivered due to de-duplication. See {@link DeviceMessage} for
     * details.
     */
    private static String getUUID(SharedPreferences sharedPreferences) {
        String uuid = sharedPreferences.getString(KEY_UUID, "");
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            sharedPreferences.edit().putString(KEY_UUID, uuid).apply();
        }
        return uuid;
    }

    /**
     * The entry point to Google Play Services.
     */
    private GoogleApiClient mGoogleApiClient;

    // Views.
    private SwitchCompat mPublishSwitch;


    /**
     * The {@link Message} object used to broadcast information about the device to nearby devices.
     */
    private Message mPubMessage;



    /**
     * Adapter for working with messages from nearby publishers.
     */
    private ArrayAdapter<String> mNearbyDevicesArrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anunciar_view);
        mPublishSwitch = (SwitchCompat) findViewById(R.id.publish_switch);
        mPubMessage = DeviceMessage.newNearbyMessage(getUUID(getSharedPreferences(
                getApplicationContext().getPackageName(), Context.MODE_PRIVATE)));

        mPublishSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If GoogleApiClient is connected, perform pub actions in response to user action.
                // If it isn't connected, do nothing, and perform pub actions when it connects (see
                // onConnected()).
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    if (isChecked) {
                        publish();
                    } else {
                        unpublish();
                    }
                }
            }
        });
        final List<String> nearbyDevicesArrayList = new ArrayList<>();
        mNearbyDevicesArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                nearbyDevicesArrayList);
        final ListView nearbyDevicesListView = (ListView) findViewById(
                R.id.nearby_devices_list_view);
        if (nearbyDevicesListView != null) {
            nearbyDevicesListView.setAdapter(mNearbyDevicesArrayAdapter);
        }
        buildGoogleApiClient();
    }

    /**
     * Publishes a message to nearby devices and updates the UI if the publication either fails or
     * TTLs.
     */
    private void publish() {
        Log.i(TAG, "Publishing");
        PublishOptions options = new PublishOptions.Builder()
                .setStrategy(PUB_SUB_STRATEGY)
                .setCallback(new PublishCallback() {
                    @Override
                    public void onExpired() {
                        super.onExpired();
                        Log.i(TAG, "No longer publishing");
                        runOnUiThread(new Runnable() {
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
                            Log.i(TAG, "Published successfully.");
                        } else {
                            logAndShowSnackbar("Could not publish, status = " + status);
                            mPublishSwitch.setChecked(false);
                        }
                    }
                });
    }

    /**
     * Stops publishing message to nearby devices.
     */
    private void unpublish() {
        Log.i(TAG, "Unpublishing.");
        Nearby.Messages.unpublish(mGoogleApiClient, mPubMessage);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "GoogleApiClient connected");
        // We use the Switch buttons in the UI to track whether we were previously doing pub/sub (
        // switch buttons retain state on orientation change). Since the GoogleApiClient disconnects
        // when the activity is destroyed, foreground pubs/subs do not survive device rotation. Once
        // this activity is re-created and GoogleApiClient connects, we check the UI and pub/sub
        // again if necessary.
        if (mPublishSwitch.isChecked()) {
            publish();

        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        logAndShowSnackbar("Connection suspended. Error code: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mPublishSwitch.setEnabled(false);

        logAndShowSnackbar("Exception while connecting to Google Play services: " +
                connectionResult.getErrorMessage());
    }

    /**
     * Builds {@link GoogleApiClient}, enabling automatic lifecycle management using
     * {@link GoogleApiClient.Builder#enableAutoManage(FragmentActivity,
     * int, GoogleApiClient.OnConnectionFailedListener)}. I.e., GoogleApiClient connects in
     * {@link AppCompatActivity#onStart}, or if onStart() has already happened, it connects
     * immediately, and disconnects automatically in {@link AppCompatActivity#onStop}.
     */

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
    private void logAndShowSnackbar(final String text) {
        Log.w(TAG, text);
        View container = findViewById(R.id.activity_main_container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

}
