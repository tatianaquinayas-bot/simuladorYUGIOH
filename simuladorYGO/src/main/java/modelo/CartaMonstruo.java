/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author 57323
 */
public abstract class CartaMonstruo {


    public static final String ATAQUE = "Ataque";
    public static final String DEFENSA = "Defensa";


    protected String nombre;
    protected int ataque;
    protected int defensa;
    protected String posicion;


    public void configurarDatos(String nombre, int ataque,
                                int defensa, String posicion) {


        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }


        if (ataque < 0 || defensa < 0) {
            throw new IllegalArgumentException(
                "ATQ y DEF no pueden ser negativos."
            );
        }


        if (!ATAQUE.equals(posicion) && !DEFENSA.equals(posicion)) {
            throw new IllegalArgumentException("Posición inválida.");
        }


        this.nombre = nombre;
        this.ataque = ataque;
        this.defensa = defensa;
        this.posicion = posicion;
    }


    public String consultarNombre() {
        return nombre;
    }


    public int consultarAtaque() {
        return ataque;
    }


    public int consultarDefensa() {
        return defensa;
    }


    public String consultarPosicion() {
        return posicion;
    }


    public abstract int calcularPoder();
}