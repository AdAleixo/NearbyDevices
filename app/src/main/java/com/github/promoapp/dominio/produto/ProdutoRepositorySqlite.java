package com.github.promoapp.dominio.produto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.promoapp.dominio.comum.PromoAppSqliteHelper;

import java.util.List;

public class ProdutoRepositorySqlite implements ProdutoRepository {

    private Context context;

    public ProdutoRepositorySqlite(Context context) {
        this.context = context;
    }

    @Override
    public Produto recuperarPorId(Long id) {
        PromoAppSqliteHelper openHelper = new PromoAppSqliteHelper(this.context);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        String[] campos = {"ID", "NOME", "DESCRICAO"};

        Cursor cursor = database.query("PRODUTO", campos, "ID = ?",
                new String[]{id.toString()}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Produto produto = criarProduto(cursor);
            database.close();
            return produto;
        } else {
            database.close();
            return null;
        }
    }

    private Produto criarProduto(Cursor cursor) {
        Produto produto = new Produto();
        produto.setId(cursor.getLong(1));
        produto.setNome(cursor.getString(2));
        produto.setDescricao(cursor.getString(3));
        return produto;
    }

    @Override
    public List<Produto> pesquisar(String termoPesquisa) {
        return null;
    }

    @Override
    public void salvar(Produto produto) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NOME", produto.getNome());
        contentValues.put("DESCRICAO", produto.getDescricao());

        PromoAppSqliteHelper openHelper = new PromoAppSqliteHelper(this.context);
        SQLiteDatabase database = openHelper.getWritableDatabase();

        if (produto.getId() == null) {
            produto.setId(database.insert("PRODUTO", null, contentValues));
        } else {
            database.update("PRODUTO", contentValues, "ID = ?",
                    new String[]{produto.getId().toString()});
        }

        database.close();
    }

    @Override
    public void excluir(Long id) {

    }
}
