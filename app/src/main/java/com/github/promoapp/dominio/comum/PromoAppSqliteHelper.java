package com.github.promoapp.dominio.comum;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PromoAppSqliteHelper extends SQLiteOpenHelper {

    public PromoAppSqliteHelper(Context context) {
        super(context, "promoapp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE PRODUTO (\n");
        sqlBuilder.append("    ID INTEGER PRIMARY KEY AUTOINCREMENT,\n");
        sqlBuilder.append("    NOME TEXT NOT NULL,\n");
        sqlBuilder.append("    DESCRICAO TEXT NOT NULL\n");
        sqlBuilder.append(")");
        db.execSQL(sqlBuilder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
