package com.example.realmapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.realmapp.Persona;

import io.realm.Realm;

public class AddContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        final EditText ETnombre = findViewById(R.id.nombrePersona);
        final EditText ETapellido = findViewById(R.id.apellidoPersona);
        final EditText ETedad = findViewById(R.id.edadPersona);
        final RadioButton CHgeneroF = findViewById(R.id.female_radioB_add);
        final RadioButton CHgeneroM = findViewById(R.id.male_radioB_add);


        Realm realm = Realm.getDefaultInstance();



        Button addButton = findViewById(R.id.anadirPersona);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = ETnombre.getText().toString();
                String apellido=  ETapellido.getText().toString();
                int edad = Integer.valueOf(ETedad.getText().toString());
                String genero = Persona.Genero.N.toString();

               if(CHgeneroF.isChecked()){

                 genero = Persona.Genero.F.toString();
               }else if(CHgeneroM.isChecked()){

                    genero = Persona.Genero.M.toString();
                }

                Persona persona = new Persona(nombre, apellido,edad,genero);

                realm.beginTransaction();
                realm.copyToRealm(persona);
                realm.commitTransaction();

                finish();
            }
        });
    }
}
