package com.example.realmapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import io.realm.Realm;
import io.realm.RealmQuery;

public class EditContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        final EditText ETnombre = findViewById(R.id.nombrePersona);
        final EditText ETapellido= findViewById(R.id.apellidoPersona);
        final EditText ETedad = findViewById(R.id.edadPersona);
        final RadioButton RBgeneroF = findViewById(R.id.F_radio_button_edit);
        final RadioButton RBgeneroM = findViewById(R.id.M_radio_button_edit);

        Realm realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("Nombre");
        RealmQuery<Persona> query = realm.where(Persona.class).contains("nombre",nombre);
        Persona persona = query.findFirst();

        if(persona != null) {
            int edad = persona.getEdad();
            String genero = persona.getGenero();

            ETnombre.setText(nombre);
            ETedad.setText(String.valueOf(edad));
            RBgeneroF.setChecked(persona.getGenero()== Persona.Genero.F.toString());
            RBgeneroM.setChecked(persona.getGenero()== Persona.Genero.M.toString());
        }

        Button guardarCambios = findViewById(R.id.guardarCambios);

        guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int edad = Integer.valueOf(ETedad.getText().toString());
                String nombre= ETnombre.getText().toString();
                String apellido= ETapellido.getText().toString();
                String genero = Persona.Genero.N.toString();

                if(RBgeneroM.isChecked()){
                    RBgeneroM.setChecked(false);
                    genero = Persona.Genero.F.toString();
                }
                if(RBgeneroM.isChecked()){
                    RBgeneroM.setChecked(false);
                    genero = Persona.Genero.M.toString();
                }

                Persona persona = new Persona(nombre,apellido,edad,genero);
                realm.beginTransaction();//iniciamos la transaccion
                realm.copyToRealmOrUpdate(persona);//si encuentra una persona con la misma primary key la actualiza, sino la crea
                realm.commitTransaction();//se envia a transaccion
                finish();//finaliza la activity
            }
        });

        Button volverAtras = findViewById(R.id.volverAtras);
        volverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
