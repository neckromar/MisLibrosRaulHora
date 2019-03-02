package com.example.mislibrosraulhora;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

public class DBAdapter {

    public static final String ROW_ID = "_id";
    public static final String TITULO = "titulo";
    public static final String AUTOR = "autor";
    public static final String EDITORIAL = "editorial";
    public static final String ISBN = "isbn";
    public static final String ANIO = "anio";
    public static final String PAGINAS = "paginas";
    public static final String EBOOK = "ebook";
    public static final String LEIDO = "leido";
    public static final String NOTA = "nota";
    public static final String RESUMEN = "resumen";

    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "MisLibrosRaulHora";
    static final String DATABASE_TABLE = "libros";
    static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "CREATE TABLE libros (_id integer primary key autoincrement, "
            + TITULO + " text, "
            + AUTOR + " text, "
            + EDITORIAL + " text, "
            + ISBN + " text, "
            + ANIO + " text, "
            + PAGINAS + " text, "
            + EBOOK + " integer, "
            + LEIDO + " integer, "
            + NOTA + " float, "
            + RESUMEN + " text "
            + ");";
    final Context mCtx;
    DatabaseHelper mDbHelper;
    SQLiteDatabase myBD;

    public DBAdapter(Context ctx) {
        this.mCtx = ctx;
        mDbHelper = new DatabaseHelper(mCtx);
    }

    public long insertar(String titulo, String autor, String editorial, String isbn, String anio,
                             String paginas, Integer ebook, Integer leido, Float nota, String resumen) {

        ContentValues campo = new ContentValues();

        campo.put(TITULO, titulo);
        campo.put(AUTOR, autor);
        campo.put(EDITORIAL, editorial);
        campo.put(ISBN, isbn);
        campo.put(ANIO, anio);
        campo.put(PAGINAS, paginas);
        campo.put(EBOOK, ebook);
        campo.put(LEIDO, leido);
        campo.put(NOTA, nota);
        campo.put(RESUMEN, resumen);

        return this.myBD.insert(DATABASE_TABLE, null, campo);
    }

    public boolean borrar(long rowId) {

        return this.myBD.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
    }

    public boolean actualizar(long rowId, String titulo, String autor, String editorial, String isbn, String anio,
                                  String paginas, Integer ebook, Integer leido, Float nota, String resumen) {
        ContentValues campos = new ContentValues();

        campos.put(TITULO, titulo);
        campos.put(AUTOR, autor);
        campos.put(EDITORIAL, editorial);
        campos.put(ISBN, isbn);
        campos.put(ANIO, anio);
        campos.put(PAGINAS, paginas);
        campos.put(EBOOK, ebook);
        campos.put(LEIDO, leido);
        campos.put(NOTA, nota);
        campos.put(RESUMEN, resumen);

        return this.myBD.update(DATABASE_TABLE, campos, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
    }

    public Cursor VERLibros() {

        return this.myBD.rawQuery("SELECT * FROM libros", null);
    }

    public Cursor VERLibrosORDENADOS() {

        return this.myBD.rawQuery("SELECT * FROM libros ORDER BY UPPER(autor)", null);
    }
    public Cursor VERLibrosORDENADOSTITULO() {

        return this.myBD.rawQuery("SELECT * FROM libros ORDER BY UPPER(titulo)", null);
    }

    public Cursor VERLibro(long rowId) {

        Cursor cursor = myBD.rawQuery("SELECT * FROM libros" + " WHERE _id = " + rowId, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int numeroLibros() throws SQLException {

        Cursor cursor = myBD.rawQuery("SELECT COUNT(*) FROM libros", null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public DBAdapter openW() throws SQLException {
        myBD = this.mDbHelper.getWritableDatabase();
        return this;
    }

    public DBAdapter openR() throws SQLException {
        myBD = mDbHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        this.mDbHelper.close();
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            onCreate(db);
        }
    }

}
