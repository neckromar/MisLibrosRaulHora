package com.example.mislibrosraulhora;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listalibros;
    Button ordenarautor;
    Button ordenartitulo;
    DBAdapter DB;
    private Cursor filas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ordenarautor=findViewById(R.id.orderbyautor);
        ordenarautor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ListaLIBROSORDENADOS();
            }
        });


        ordenartitulo=findViewById(R.id.orderbytitulo);
        ordenartitulo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ListaLIBROSORDENADOSTITULO();
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailLibro.class);
                intent.putExtra("id", 0);
                startActivity(intent);
            }
        });

        DB = new DBAdapter(this);
        Ejemplos();
        ListaACT();

        listalibros.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DetailLibro.class);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        ListaACT();
    }

    public void ListaLIBROSORDENADOS() {

        try {
            DB.openR();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listalibros =  findViewById(R.id.listalibros);
        filas = DB.VERLibrosORDENADOS();

        if (filas.moveToFirst())
        {
            Adp adaptador = new Adp(this, filas, 0);
            listalibros.setAdapter(adaptador);
        }
        else
        {
            listalibros.removeAllViewsInLayout();
        }
        DB.close();
    }


    public void ListaLIBROSORDENADOSTITULO() {

        try {
            DB.openR();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listalibros =  findViewById(R.id.listalibros);
        filas = DB.VERLibrosORDENADOSTITULO();

        if (filas.moveToFirst())
        {
            Adp adaptador = new Adp(this, filas, 0);
            listalibros.setAdapter(adaptador);
        }
        else
        {
            listalibros.removeAllViewsInLayout();
        }
        DB.close();
    }

    public void ListaACT() {

        try {
            DB.openR();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listalibros =  findViewById(R.id.listalibros);
        filas = DB.VERLibros();

        if (filas.moveToFirst())
        {
            Adp adaptador = new Adp(this, filas, 0);
            listalibros.setAdapter(adaptador);
        }
        else
        {
            listalibros.removeAllViewsInLayout();
        }
        DB.close();
    }

    //Si no existieran libros en la BD entonces se insertaria dos o tres por defecto para no tener que crearlos manualmente
    public void Ejemplos() {

        try {
            DB.openW();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (DB.numeroLibros() == 0)
            {
                DB.insertar("Cómo hacer que te pasen cosas buenas", "Marián Rojas Estapé", "Espasa", "9788467053302", "2018",
                        "232", 1, 1, 3f, "Uniendo el punto de vista científico, psicológico y humano, la autora nos ofrece una reflexión profunda, salpicada de útiles consejos y con vocación eminentemente didáctica, acerca de la aplicación de nuestras propias capacidades al empeño de procurarnos una existencia plena y feliz: conocer y optimizar determinadas\n" +
                                "zonas del cerebro, fijar metas y objetivos en la vida, ejercitar la voluntad, poner en marcha la inteligencia emocional, desarrollar la asertividad, evitar el exceso de autocrítica y autoexigencia, reivindicar el papel del optimismo…");
                DB.insertar("Memorias de una salvaje", "@Srtabebi ", "Planeta", "9788408194453", "2018",
                        "448", 1, 1, 4f, "K tiene 19 años y una vida un tanto peculiar. Cuando su padre es asesinado en un ajuste de cuentas, se ve obligada a compaginar sus estudios con un trabajo muy poco convencional: el de recepcionista y chica de los recados en un local de alterne clandestino. ");
                DB.insertar("Yo, Julia", "Santiago Posteguillo ", "Planeta", "9788408197409", "2018",
                        "704", 1, 0, 5f, "192 d.C. Varios hombres luchan por un imperio, pero Julia, hija de reyes, madre de césares y esposa de emperador, piensa en algo más grande: una dinastía. Roma está bajo el control de Cómodo, un emperador loco. El Senado se conjura para terminar con el tirano y los gobernadores militares más poderosos podrían dar un golpe de Estado: Albino en Britania, Severo en el Danubio o Nigro en Siria. ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DB.close();
    }
}
