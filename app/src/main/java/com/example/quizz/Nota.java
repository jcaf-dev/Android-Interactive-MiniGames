package com.example.quizz;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Nota {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String Nombre;
    private String Contraseya;
   private int jugadas;
    private int puntuacion;

    public Nota(String Nombre, String Contraseya) {
        this.Nombre = Nombre;
        this.Contraseya = Contraseya;
        jugadas=0;
        puntuacion=0;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setJugadas(int jugadas) {
        this.jugadas = jugadas;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getContraseya() {
        return Contraseya;
    }

    public int getJugadas() {
        return jugadas;
    }

    public int getPuntuacion() {
        return puntuacion;
    }
}
