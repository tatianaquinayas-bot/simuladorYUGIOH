/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package principal;

/**
 *
 * @author 57323
 */
import java.util.ArrayList;
import java.util.List;

import modelo.CartaMonstruo;
import modelo.Jugador;
import modelo.MonstruoNormal;
import modelo.Duelo;

import vista.VentanaPrincipal;

public class SimuladorYGO {

    public static void main(String[] args) {

        // Estructura dinámica de jugadores
        ArrayList<Jugador> jugadores = new ArrayList<>();

        // Estructura dinámica de monstruos
        ArrayList<CartaMonstruo> monstruos = new ArrayList<>();

        // Crear duelo de demostración
        Duelo duelo = new Duelo();

        duelo.configurarJugadores("Yugi", "Kaiba");

        jugadores.add(duelo.consultarJugador1());
        jugadores.add(duelo.consultarJugador2());

        monstruos.add(
            new MonstruoNormal(
                "Dragon Blanco de Ojos Azules",
                3000, 2500,
                CartaMonstruo.ATAQUE
            )
        );

        monstruos.add(
            new MonstruoNormal(
                "Mago Oscuro",
                2500, 2100,
                CartaMonstruo.ATAQUE
            )
        );

        // Primer reporte
        generarReporteJugadores(jugadores);

        // Segundo reporte
        generarReporteMonstruos(monstruos);

        // Iniciar aplicación gráfica
        java.awt.EventQueue.invokeLater(() -> {

            new VentanaPrincipal().setVisible(true);

        });
    }

    public static void generarReporteJugadores(
            List<Jugador> jugadores) {

        System.out.println("\n========== REPORTE DE JUGADORES ==========");

        for (Jugador jugador : jugadores) {

            System.out.println(jugador.toString());
        }

        System.out.println("==========================================");
    }

    public static void generarReporteMonstruos(
            List<CartaMonstruo> monstruos) {

        System.out.println("\n========== REPORTE DE MONSTRUOS ==========");

        for (CartaMonstruo monstruo : monstruos) {

            System.out.println(
                "Nombre: " + monstruo.consultarNombre()
            );

            System.out.println(
                "Ataque: " + monstruo.consultarAtaque()
            );

            System.out.println(
                "Defensa: " + monstruo.consultarDefensa()
            );

            System.out.println(
                "Poder: " + monstruo.calcularPoder()
            );

            System.out.println("------------------------------------------");
        }
    }
}
