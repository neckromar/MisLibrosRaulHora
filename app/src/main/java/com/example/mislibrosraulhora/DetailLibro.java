package com.example.mislibrosraulhora;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.sql.SQLException;


public class DetailLibro extends AppCompatActivity {

    boolean insertar, editar, eliminar;
    long ID;
    EditText ed_titulo;
    EditText ed_autor;
    EditText ed_editorial;
    EditText ed_isbn;
    EditText ed_paginas;
    EditText ed_anio;
    CheckBox cb_ebook;
    CheckBox cb_leido;
    RatingBar nota;
    EditText ed_resumen;
    private DBAdapter DB = new DBAdapter(this);
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_libro);

        ed_titulo = findViewById(R.id.edit_titulo);
        ed_autor = findViewById(R.id.edit_autor);
        ed_editorial = findViewById(R.id.edit_editorial);
        ed_isbn = findViewById(R.id.edit_isbn);
        ed_paginas = findViewById(R.id.edit_paginas);
        ed_anio = findViewById(R.id.edit_anio);
        cb_ebook = findViewById(R.id.cb_ebook);
        cb_leido = findViewById(R.id.cb_leido);
        nota = findViewById(R.id.nota);
        ed_resumen = findViewById(R.id.edit_resumen);

        ID = getIntent().getLongExtra("id", 0);

        if (ID != 0) {

            try {
                DB.openR();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            insertar = false;
            editar = true;
            eliminar = true;

            cursor = DB.VERLibro(ID);

            ed_titulo.setText(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
            ed_autor.setText(cursor.getString(cursor.getColumnIndexOrThrow("autor")));
            ed_editorial.setText(cursor.getString(cursor.getColumnIndexOrThrow("editorial")));
            ed_isbn.setText(cursor.getString(cursor.getColumnIndexOrThrow("isbn")));
            ed_paginas.setText(cursor.getString(cursor.getColumnIndexOrThrow("paginas")));
            ed_anio.setText(cursor.getString(cursor.getColumnIndexOrThrow("anio")));
            cb_ebook.setChecked(cursor.getInt(cursor.getColumnIndexOrThrow("ebook")) != 0);
            cb_leido.setChecked(cursor.getInt(cursor.getColumnIndexOrThrow("leido")) != 0);
            nota.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow("nota")));
            ed_resumen.setText(cursor.getString(cursor.getColumnIndexOrThrow("resumen")));

            DB.close();
        } else {
            insertar = true;
            editar = false;
            eliminar = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.guardar).setVisible(insertar);
        menu.findItem(R.id.eliminar).setVisible(eliminar);
        menu.findItem(R.id.edit).setVisible(editar);
        return true;
    }

    //PARA VOLVER A LA ACTIVITY PRINCIPAL(MAIN ACTIVITY) PARA QUE NO SE QUEDE EN LA MISMA PANTALLA
    public void volver() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit:
                Actualizar();

                break;
            case R.id.guardar: {
                Insertar();

                break;
            }
            case R.id.eliminar: {

                    lanzaralert();

                break;
            }
        }
        return true;
    }


    public void Insertar() {
        try {
            if (ed_titulo.getText().toString().matches("") || ed_autor.getText().toString().matches("")) {
                Toast.makeText(this, "Introduce el título y el autor", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    DB.openW();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                DB.insertar(
                        ed_titulo.getText().toString(),
                        ed_autor.getText().toString(),
                        ed_editorial.getText().toString(),
                        ed_isbn.getText().toString(),
                        ed_anio.getText().toString(),
                        ed_paginas.getText().toString(),
                        (cb_ebook.isChecked() ? 1 : 0),
                        (cb_leido.isChecked() ? 1 : 0),
                        nota.getRating(),
                        ed_resumen.getText().toString());

                DB.close();

                Toast.makeText(this, ed_titulo.getText().toString() + "  añadido ", Toast.LENGTH_SHORT).show();

                volver();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void lanzaralert(){

        new AlertDialog.Builder(this)
                .setTitle("Confirmacion")
                .setMessage("Estas seguro de borrar el libro "+ed_titulo.getText().toString().toUpperCase()+" ? ")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        Eliminar();

                    }})
                .setNegativeButton("Cancelar", null)
                .show();
    }


    public void Eliminar() {
        try {
            try {
                DB.openW();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            DB.borrar(ID);
            Toast.makeText(this, " El Libro "+ed_titulo.getText().toString().toUpperCase() + " ha sido eliminado !! ", Toast.LENGTH_SHORT).show();
            DB.close();

            volver();

        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void Actualizar() {
        try {
            if (ed_titulo.getText().toString().matches("") || ed_autor.getText().toString().matches("")) {
                Toast.makeText(this, "Introduce el título y el autor", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    DB.openW();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                DB.actualizar(ID,
                        ed_titulo.getText().toString(),
                        ed_autor.getText().toString(),
                        ed_editorial.getText().toString(),
                        ed_isbn.getText().toString(),
                        ed_anio.getText().toString(),
                        ed_paginas.getText().toString(),
                        (cb_ebook.isChecked() ? 1 : 0),
                        (cb_leido.isChecked() ? 1 : 0),
                        nota.getRating(),
                        ed_resumen.getText().toString()
                );

                Toast.makeText(this, ed_titulo.getText().toString() + " actualizado ", Toast.LENGTH_SHORT).show();
                DB.close();

                volver();

            }

        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }






}
