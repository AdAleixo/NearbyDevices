package com.github.promoapp.dominio.promocao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.promoapp.dominio.anuncio.Anuncio;
import com.github.promoapp.dominio.comum.PromoAppSqliteHelper;

import java.util.ArrayList;
import java.util.List;

public class PromocaoRepository {

    private Context context;

    public PromocaoRepository(Context context) {
        this.context = context;
    }

    public List<Anuncio> recuperarTodos() {
        List<Anuncio> anuncios = new ArrayList<>();
        PromoAppSqliteHelper openHelper = new PromoAppSqliteHelper(this.context);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        String[] campos = {"ID", "NOME", "DESCRICAO", "PRECO", "URL", "VALIDADE"};

        Cursor cursor = database.query("ANUNCIO_RECEBIDO", campos, null,
                null, null, null, "VALIDADE DESC");

        database.close();
        return anuncios;
    }

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
            anuncio.setId(database.insert("ANUNCIO_RECEBIDO", null,
                    contentValues));
        } else {
            database.update("ANUNCIO_RECEBIDO", contentValues, "ID = ?",
                    new String[]{anuncio.getId().toString()});
        }

        database.close();
    }
}
