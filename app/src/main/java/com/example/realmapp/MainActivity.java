package com.example.realmapp;


import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {


    //TODO: En el MainActivity, mostrar la agenda, y un botón de añadir contacto.
    //TODO: Arriba del todo tener el filtro.

    @RequiresApi(api = Build.VERSION_CODES.N)//por el foreach
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializamos realm.
        Realm.init(this);
        final RealmConfiguration configuration = new RealmConfiguration.Builder().name("sample.realm").schemaVersion(1).build();
        Realm.setDefaultConfiguration(configuration);
        Realm.getInstance(configuration);
        //Creamos objeto realm.
        Realm realm = Realm.getDefaultInstance();

        //Indicamos los contactos que queremos cargar en la lista y los almacenamos en el adaptador.
        OrderedRealmCollection orderedRealmCollection = realm.where(Persona.class).findAll().sort("nombre");
        PersonaRealmAdapter personaRealmAdapter = new PersonaRealmAdapter(orderedRealmCollection,true);

        //Identificamos el recyclerview en la vista, y le seteamos una Layout.
        RecyclerView recyclerView = findViewById(R.id.contactos);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        //Seteamos el adaptador al recyclerView.
        recyclerView.setAdapter(personaRealmAdapter);

        //Identificamos el  FloatingActionButton y le añadimos un listener.
        FloatingActionButton floatingActionButton = findViewById(R.id.addContact);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddContactActivity.class);
                startActivity(intent);
            }
        });

        //Identificamos el botoón de restablecer filtro de búsqueda.
        Button buttonRestablecer = findViewById(R.id.restablecer);

        //Identificamos campos del filtro de búsqueda.
        TextView TVEdad1 = findViewById(R.id.edad1);
        TextView TVEdad2 = findViewById(R.id.edad2);
        CheckBox checkBoxAmbos = findViewById(R.id.ambos);
        CheckBox checkBoxHombre = findViewById(R.id.hombre);
        CheckBox checkBoxMujer = findViewById(R.id.mujer);


        /*Listener del botón de restablecerer, donde seteamos todos los campos de búsqueda a valores nulos/vacios
        y además, seteamos al recyclerView el adaptador por defecto.
         */
        buttonRestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TVEdad1.setText("");
                TVEdad2.setText("");
                checkBoxHombre.setChecked(false);
                checkBoxMujer.setChecked(false);
                checkBoxAmbos.setChecked(false);
                recyclerView.setAdapter(personaRealmAdapter);//muestra la base de datos sin filtros
            }
        });

        Button buttonFiltrar = findViewById(R.id.filtrar);

        checkBoxMujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxAmbos.setChecked(false);
                checkBoxHombre.setChecked(false);
            }
        });
        checkBoxHombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxMujer.setChecked(false);
                checkBoxAmbos.setChecked(false);
            }
        });

        checkBoxAmbos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxHombre.setChecked(false);
                checkBoxMujer.setChecked(false);
            }
        });

        //Listener del botón filtrar.
        buttonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos las variables para el filtro de la edad, e intentamos leerlas.
                int edad1,edad2;
                try {
                    edad1 = Integer.valueOf(TVEdad1.getText().toString());
                    edad2 = Integer.valueOf(TVEdad2.getText().toString());

                }catch (NumberFormatException nfe) {
                    edad1 = 0;
                    edad2 = 0;
                }

                //Identificamos el estado de los checkBoxAmbos
                boolean stateCbHombre = checkBoxHombre.isChecked();
                boolean stateCbMujer = checkBoxMujer.isChecked();

                //Creamos la query a realizar.
                RealmQuery<Persona> query = null;

                //Si el rango de edades es> 0, buscaremos dentro de ese rango.
                if(edad1 > 0 && edad2 > 0) {
                    query = realm.where(Persona.class).between("edad",edad1,edad2);
                }
                //Si el Checkbox de hombre está seleccionado buscaremos por ese género.
                if(stateCbHombre) {
                    if(query != null) {
                        query = query.and().contains("genero","M");
                    }else {
                        query = realm.where(Persona.class).contains("genero","M");
                    }
                }else if(stateCbMujer) {
                    if(query != null) {
                        query = query.and().contains("genero","F");
                    }else {
                        query = realm.where(Persona.class).contains("genero","F");
                    }
                //Si no se ha marcado ningun Checkbox
                }else {
                    if(query!=null) {
                        query = query.sort("nombre");
                      }else {
                        query = realm.where(Persona.class).sort("nombre");
                      }
                  }

                  //Introducimos la query en el resultado, lo introducimos en el adaptador, y lo seteamos al recyclerView.
                  RealmResults<Persona> result = query.findAll();
                  PersonaRealmAdapter personaRealmAdapter = new PersonaRealmAdapter(result,true);
                  recyclerView.setAdapter(personaRealmAdapter);
            }
        });
    }
}
