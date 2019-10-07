package com.github.promoapp.dominio.anuncio;

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
    private final Anuncio mAnuncio;

    /**
     * Builds a new {@link Message} object using a unique identifier.
     */
    public static Message newNearbyMessage(String instanceId, Anuncio anuncio) {
        AnuncioMessage anuncioMessage = new AnuncioMessage(instanceId, anuncio);
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

    private AnuncioMessage(String uuid, Anuncio anuncio) {
        mUUID = uuid;
        mAnuncio = anuncio;
    }

    public Anuncio getAnuncio() {
        return mAnuncio;
    }
}