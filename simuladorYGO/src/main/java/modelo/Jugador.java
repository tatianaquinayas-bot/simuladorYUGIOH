/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author 57323
 */
public class Jugador implements atacable {


    public static final int LP_INICIALES = 8000;


    private String nombre;
    private int puntosVida;


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {


        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "El nombre del jugador es obligatorio."
            );
        }


        this.nombre = nombre;
    }


    public int getPuntosVida() {
        return puntosVida;
    }


    public void setPuntosVida(int puntosVida) {


        if (puntosVida < 0) {
            throw new IllegalArgumentException(
                "Los puntos de vida no pueden ser negativos."
            );
        }


        this.puntosVida = puntosVida;
    }


    @Override
    public void recibirDanio(int danio) {


        if (danio < 0) {
            throw new IllegalArgumentException(
                "El daño no puede ser negativo."
            );
        }


        puntosVida = Math.max(0, puntosVida - danio);
    }


    public boolean estaDerrotado() {
        return puntosVida <= 0;
    }


    @Override
    public String toString() {


        return "Jugador: " + nombre
                + " | LP: " + puntosVida;
    }
}