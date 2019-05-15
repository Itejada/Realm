package com.example.realmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.realmapp.Persona;

import io.realm.Realm;
import io.realm.RealmQuery;

public class ShowContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);

        final TextView TVnombre = findViewById(R.id.nombrePersona);
        final TextView TVedad = findViewById(R.id.edadPersona);
        final TextView TVgenero = findViewById(R.id.Tv_genero_detail);

        Realm realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("Nombre");
        RealmQuery<Persona> query = realm.where(Persona.class).contains("nombre",nombre);
        Persona persona = query.findFirst();

        if(persona != null) {
            int edad = persona.getEdad();
            String genero = persona.getGenero();

            TVnombre.setText(nombre);
            TVedad.setText(String.valueOf(edad));
            TVgenero.setText(String.valueOf(genero));
        }

        Button volverAtras = findViewById(R.id.volverAtras);
        volverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
