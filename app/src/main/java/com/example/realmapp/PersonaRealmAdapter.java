package com.example.realmapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

public class PersonaRealmAdapter extends RealmRecyclerViewAdapter {

    public static final String NOMBRE ="Nombre";
    public static final String APELLIDO ="Apellido";

    OrderedRealmCollection data;
    String[] opciones = {"Visualizar Contacto", "Editar Contacto", "Borrar Contacto", "Salir"};
    Realm realm = Realm.getDefaultInstance();

    public PersonaRealmAdapter(@Nullable OrderedRealmCollection data, boolean autoUpdate) {
        super(data, autoUpdate);
        this.data = data;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Persona persona = (Persona) data.get(i);
        Context context = viewHolder.itemView.getContext();

        ((ContactoViewHolder) viewHolder).nombre.setText(persona.getNombre());
        ((ContactoViewHolder) viewHolder).edad.setText(String.valueOf(persona.getEdad()));
        ((ContactoViewHolder) viewHolder).genero.setText(String.valueOf(persona.getGenero()));


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShowContactActivity.class);
                intent.putExtra("Nombre", persona.getNombre());
                v.getContext().startActivity(intent);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                android.app.AlertDialog.Builder dialogo = new AlertDialog.Builder(context);//una ventana emergente
                dialogo.setTitle("¿Que acción deseas realizar?").setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.app.AlertDialog.Builder afirmacion = new AlertDialog.Builder(context);
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(context, ShowContactActivity.class);
                                intent.putExtra(NOMBRE, persona.getNombre());
                                intent.putExtra(APELLIDO, persona.getApellido());
                                context.startActivity(intent);
                                break;
                            case 1:
                                afirmacion.setMessage("¿Deseas editarlo?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(context, EditContactActivity.class);
                                        intent.putExtra(NOMBRE, persona.getNombre());
                                        intent.putExtra(APELLIDO, persona.getApellido());
                                        context.startActivity(intent);
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                afirmacion.create();
                                afirmacion.show();
                                break;
                            case 2:
                                afirmacion.setMessage("¿Deseas borrar el contacto?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        realm.beginTransaction();
                                        data.deleteFromRealm(i);
                                        realm.commitTransaction();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                afirmacion.create();
                                afirmacion.show();
                                break;
                            case 3:
                                break;
                        }
                    }
                });
                dialogo.create();
                dialogo.show();
                return true;
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemContacto = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.persona_item, viewGroup, false);
        return new ContactoViewHolder(itemContacto);
    }

    class ContactoViewHolder extends RecyclerView.ViewHolder {
        private TextView nombre;
        private TextView edad;
        private TextView genero;

        ContactoViewHolder(View item_contacto) {
            super(item_contacto);
            nombre = item_contacto.findViewById(R.id.nombrePersona);
            edad = item_contacto.findViewById(R.id.edadPersona);
            genero = item_contacto.findViewById(R.id.generoPersona);


        }
    }

}
