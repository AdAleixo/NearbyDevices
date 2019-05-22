package com.google.android.gms.nearby.messages.samples.nearbydevices;

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


public class MainActivity extends AppCompatActivity  {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // addListenerOnButtonAnunciar();
       // addListenerOnButtonVerOferta();

        /* TESTE
        button = (Button) findViewById(R.id.buttonAnunciar);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openTelaAnuncio();
            }
        });
        */
    }

    //Button Anunciar
    public void openTelaAnuncio(View view) {
        Intent intent = new Intent(MainActivity.this, TelaAnuncio.class);
        startActivity(intent);
    }

    //Button Ofertas
    public void openTelaOfertas(View view) {
        Intent intent = new Intent(MainActivity.this, TelaOferta.class);
        startActivity(intent);
    }

    //Button CRIAR ANUNCIO AINDA NAO FEITA A TELA
    /*
    public void openTelaOfertas(View view) {
        Intent intent = new Intent(MainActivity.this, TelaOferta.class);
        startActivity(intent);
    }
    */



 /*
        //Activates the button "Anunciar"
    public void addListenerOnButtonAnunciar() {

        button = (Button) findViewById(R.id.buttonAnunciar);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                startActivity(new Intent(MainActivity.this, TelaAnuncio.class));

            }
        });
    }


    //Activates the button "Ver Oferta"

    public void addListenerOnButtonVerOferta() {

        button = (Button) findViewById(R.id.buttonAnunciar);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                startActivity(new Intent(MainActivity.this, TelaOferta.class));

            }
        });
    }
    */


}