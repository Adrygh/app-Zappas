package com.example.zappas.modelo;

public class Zapatilla {

// Declaramos los atributos de la clase zapatilla
    private String id;
    private String nombre;
    private String marca;
    private String color;

// Generamos un constructor vacio
    public Zapatilla() {
    }
// Generamos los Getters y Setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

 //Generamos el metodo toString  y decimos que nos retorne el hombre

    @Override
    public String toString() {
        return nombre;
    }
}
