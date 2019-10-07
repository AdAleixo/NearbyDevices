package com.github.promoapp.apresentacao.comum;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

public class PromoAppLogger {

    public static void logarExibirSnackbar(View view, String text) {
        Log.w(view.getClass().getSimpleName(), text);

        if (view != null) {
            Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
        }
    }
}
