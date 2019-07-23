package com.github.promoapp.dominio.anuncio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.promoapp.dominio.comum.PromoAppSqliteHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnuncioRepositorySqlite implements AnuncioRepository {

    private Context context;

    public AnuncioRepositorySqlite(Context context) {
        this.context = context;
    }

    @Override
    public List<Anuncio> recuperarTodos() {
        List<Anuncio> anuncios = new ArrayList<>();
        PromoAppSqliteHelper openHelper = new PromoAppSqliteHelper(this.context);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        String[] campos = {"NOME", "DESCRICAO", "PRECO", "URL", "VALIDADE"};

        Cursor cursor = database.query("ANUNCIO", campos, null,
                null, null, null, "VALIDADE DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                anuncios.add(criarAnuncio(cursor));
            }
        }

        database.close();
        return anuncios;
    }

    private Anuncio criarAnuncio(Cursor cursor) {
        Anuncio anuncio = new Anuncio();
        anuncio.setId(cursor.getLong(1));
        anuncio.setNome(cursor.getString(2));
        anuncio.setDescricao(cursor.getString(3));
        anuncio.setPreco(cursor.getDouble(4));
        anuncio.setUrl(cursor.getString(5));
        anuncio.setValidade(new Date(cursor.getLong(6)));
        return anuncio;
    }

    @Override
    public void salvar(Anuncio anuncio) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NOME", anuncio.getNome());
        contentValues.put("DESCRICAO", anuncio.getDescricao());
        contentValues.put("PRECO", anuncio.getPreco());
        contentValues.put("URL", anuncio.getUrl());
        contentValues.put("VALIDADE", anuncio.getValidade().getTime());

        PromoAppSqliteHelper openHelper = new PromoAppSqliteHelper(this.context);
        SQLiteDatabase database = openHelper.getWritableDatabase();

        if (anuncio.getId() == null) {
            anuncio.setId(database.insert("ANUNCIO", null, contentValues));
        } else {
            database.update("ANUNCIO", contentValues, "ID = ?",
                    new String[]{anuncio.getId().toString()});
        }

        database.close();
    }
}
