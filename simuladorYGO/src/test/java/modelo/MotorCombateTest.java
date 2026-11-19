/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author 57323
 */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MotorCombateTest {
    private MotorCombate motor = new MotorCombate();
    private Jugador crearJugador(String nombre) {
        Jugador jugador = new Jugador();
        jugador.setNombre(nombre);
        jugador.setPuntosVida(8000);
        return jugador;
    }


    @Test
    public void probarAtaqueMayor() {
        CartaMonstruo atacante = new MonstruoNormal(
            "Dragon Blanco", 3000, 2500,
            CartaMonstruo.ATAQUE
        );
        CartaMonstruo defensor = new MonstruoNormal(
            "Mago Oscuro", 2500, 2100,
            CartaMonstruo.ATAQUE
        );
        Jugador jugador1 = crearJugador("Yugi");
        Jugador jugador2 = crearJugador("Kaiba");
        RegistroCombate resultado = motor.resolverAtaque(
            atacante, defensor, jugador1, jugador2
        );
        assertEquals(500, resultado.danio);
        assertEquals(8000, jugador1.getPuntosVida());
        assertEquals(7500, jugador2.getPuntosVida());
    }


    @Test
    public void probarAtaqueIgual() {
        CartaMonstruo atacante = new MonstruoNormal(
            "Mago A", 2000, 1500,
            CartaMonstruo.ATAQUE
        );
        CartaMonstruo defensor = new MonstruoNormal(
            "Mago B", 2000, 1800,
            CartaMonstruo.ATAQUE
        );
        Jugador jugador1 = crearJugador("Yugi");
        Jugador jugador2 = crearJugador("Kaiba");
        RegistroCombate resultado = motor.resolverAtaque(
            atacante, defensor, jugador1, jugador2
        );
        assertEquals(0, resultado.danio);
        assertEquals(8000, jugador1.getPuntosVida());
        assertEquals(8000, jugador2.getPuntosVida());
    }


    @Test
    public void probarAtaqueMenor() {
        CartaMonstruo atacante = new MonstruoNormal(
            "Mago", 1500, 1000,
            CartaMonstruo.ATAQUE
        );
        CartaMonstruo defensor = new MonstruoNormal(
            "Dragon", 2500, 2000,
            CartaMonstruo.ATAQUE
        );
        Jugador jugador1 = crearJugador("Yugi");
        Jugador jugador2 = crearJugador("Kaiba");
        RegistroCombate resultado = motor.resolverAtaque(
            atacante, defensor, jugador1, jugador2
        );
        assertEquals(1000, resultado.danio);
        assertEquals(7000, jugador1.getPuntosVida());
    }


    @Test
    public void probarAtaqueContraDefensa() {
        CartaMonstruo atacante = new MonstruoNormal(
            "Dragon", 3000, 2500,
            CartaMonstruo.ATAQUE
        );
        CartaMonstruo defensor = new MonstruoNormal(
            "Muro", 1000, 3000,
            CartaMonstruo.DEFENSA
        );
        Jugador jugador1 = crearJugador("Yugi");
        Jugador jugador2 = crearJugador("Kaiba");
        RegistroCombate resultado = motor.resolverAtaque(
            atacante, defensor, jugador1, jugador2
        );
        assertEquals(0, resultado.danio);
        assertEquals(8000, jugador2.getPuntosVida());
    }


    @Test
    public void probarAtaqueDirecto() {

        CartaMonstruo atacante = new MonstruoNormal(
            "Dragon", 3000, 2500,
            CartaMonstruo.ATAQUE
        );
        Jugador jugador1 = crearJugador("Yugi");
        Jugador jugador2 = crearJugador("Kaiba");
        RegistroCombate resultado = motor.resolverAtaque(
            atacante, jugador1, jugador2
        );
        assertEquals(3000, resultado.danio);
        assertEquals(5000, jugador2.getPuntosVida());
    }
}
