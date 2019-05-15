package com.example.realmapp;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Persona extends RealmObject {

    public enum Genero{
        M,F,N
    }

    @PrimaryKey
    private String nombre,apellido;

    private int edad;
    private String genero;


    public Persona() {}

    public Persona(String nombre, String apellido, int edad, String genero) {
        this.nombre=nombre;
        this.apellido=apellido;
        this.edad=edad;
        this.genero=genero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero.toString();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
