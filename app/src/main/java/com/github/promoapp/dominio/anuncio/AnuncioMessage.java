package com.github.promoapp.dominio.anuncio;
import android.os.Build;

import com.google.android.gms.nearby.messages.Message;
import com.google.gson.Gson;

import java.nio.charset.Charset;

/**
 * Used to prepare the payload for a
 * {@link com.google.android.gms.nearby.messages.Message Nearby Message}. Adds a unique id
 * to the Message payload, which helps Nearby distinguish between multiple devices with
 * the same model name.
 */
public class AnuncioMessage {
    private static final Gson gson = new Gson();

    private final String mUUID;
    private final String mMessageBody;
    private final String mMessageUrl;

    /**
     * Builds a new {@link Message} object using a unique identifier.
     */
    public static Message newNearbyMessage(String instanceId) {
        AnuncioMessage anuncioMessage = new AnuncioMessage(instanceId);
        return new Message(gson.toJson(anuncioMessage).getBytes(Charset.forName("UTF-8")));
    }

    /**
     * Creates a {@code DeviceMessage} object from the string used to construct the payload to a
     * {@code Nearby} {@code Message}.
     */
    public static AnuncioMessage fromNearbyMessage(Message message) {
        String nearbyMessageString = new String(message.getContent()).trim();
        return gson.fromJson(
                (new String(nearbyMessageString.getBytes(Charset.forName("UTF-8")))),
                AnuncioMessage.class);
    }

    private AnuncioMessage(String uuid) {
        mUUID = uuid;
        mMessageBody = Build.MODEL;
        mMessageUrl = "https://www.facebook.com";
        // TODO(developer): add other fields that must be included in the Nearby Message payload.
    }

    public String getMessageBody() {
        return mMessageBody + " " + mMessageUrl;
    }
}