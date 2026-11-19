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


public class JugadorTest {


    @Test
    public void probarPuntosIniciales() {


        Jugador jugador = new Jugador();


        jugador.setNombre("Yugi");
        jugador.setPuntosVida(Jugador.LP_INICIALES);


        assertEquals(8000, jugador.getPuntosVida());
    }


    @Test
    public void probarReduccionDeVida() {


        Jugador jugador = new Jugador();


        jugador.setPuntosVida(8000);
        jugador.recibirDanio(1500);


        assertEquals(6500, jugador.getPuntosVida());
    }


    @Test
    public void probarQueLosLPNoSeanNegativos() {


        Jugador jugador = new Jugador();


        jugador.setPuntosVida(1000);
        jugador.recibirDanio(2000);


        assertEquals(0, jugador.getPuntosVida());
    }


    @Test
    public void probarDanioNegativo() {


        Jugador jugador = new Jugador();


        assertThrows(
            IllegalArgumentException.class,
            () -> jugador.recibirDanio(-100)
        );
    }
}