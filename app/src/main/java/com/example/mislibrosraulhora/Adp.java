package com.example.mislibrosraulhora;


import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class Adp extends CursorAdapter {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Adp(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.mostrar_librosinfo, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView titulo =  view.findViewById(R.id.tituloportada);
        TextView autor = view.findViewById(R.id.autorportada);
        RatingBar nota = view.findViewById(R.id.nota);

        String tit = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
        String aut = cursor.getString(cursor.getColumnIndexOrThrow("autor"));
        Float not = cursor.getFloat(cursor.getColumnIndexOrThrow("nota"));

        titulo.setText(tit);
        autor.setText(aut);
        nota.setRating(not);


    }
}