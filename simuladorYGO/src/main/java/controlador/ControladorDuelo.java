/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author 57323
 */
import modelo.*;
import servicios.HistorialService;
import servicios.YGOPRODeckService;
import java.io.IOException;



public class ControladorDuelo {

    private Duelo duelo = new Duelo();
    private HistorialService historialService =
            new HistorialService();

    private YGOPRODeckService api =
            new YGOPRODeckService();
    
    public void iniciarDuelo(String nombre1, String nombre2) {


        duelo.configurarJugadores(nombre1, nombre2);
    }


    public void registrarMonstruo(
            String nombre,
            int ataque,
            int defensa,
            String posicion,
            int jugador) {


        CartaMonstruo monstruo = new MonstruoNormal(
            nombre, ataque, defensa, posicion
        );


        duelo.registrarMonstruo(monstruo, jugador);
    }

    public CartaMonstruo buscarCartaAPI(String nombre)
            throws Exception {
        return api.buscarCarta(nombre);
    }

    public RegistroCombate realizarAtaque(
            int jugador,
            int indiceAtacante,
            int indiceDefensor) throws IOException {
        RegistroCombate registro;
        if (jugador == 1) {


            if (indiceAtacante < 0
                    || indiceAtacante >=
                    duelo.consultarMonstruosJugador1().size()) {


                throw new IllegalArgumentException(
                    "Selecciona un atacante válido."
                );
            }

            CartaMonstruo atacante =
                duelo.consultarMonstruosJugador1()
                    .get(indiceAtacante);

            if (indiceDefensor == -1) {

                registro = duelo.atacarDirectamente(
                    atacante, jugador
                );

            } else {
                if (indiceDefensor >=
                        duelo.consultarMonstruosJugador2().size()) {


                    throw new IllegalArgumentException(
                        "Selecciona un defensor válido."
                    );
                }

                CartaMonstruo defensor =
                    duelo.consultarMonstruosJugador2()
                        .get(indiceDefensor);

                registro = duelo.atacar(
                    atacante, defensor, jugador
                );
            }

        } else if (jugador == 2) {
            if (indiceAtacante < 0
                    || indiceAtacante >=
                    duelo.consultarMonstruosJugador2().size()) {

                throw new IllegalArgumentException(
                    "Selecciona un atacante válido."
                );
            }

            CartaMonstruo atacante =
                duelo.consultarMonstruosJugador2()
                    .get(indiceAtacante);

            if (indiceDefensor == -1) {
                registro = duelo.atacarDirectamente(
                    atacante, jugador
                );

            } else {
                if (indiceDefensor >=
                        duelo.consultarMonstruosJugador1().size()) {
                    throw new IllegalArgumentException(
                        "Selecciona un defensor válido."
                    );
                }

                CartaMonstruo defensor =
                    duelo.consultarMonstruosJugador1()
                        .get(indiceDefensor);
                registro = duelo.atacar(
                    atacante, defensor, jugador
                );
            }


        } else {


            throw new IllegalArgumentException(
                "Jugador inválido."
            );
        }
        historialService.guardar(registro);


        return registro;
    }


    public Duelo consultarDuelo() {
        return duelo;
    }

    public String generarReporteLP() {
        return duelo.consultarJugador1().toString()
                + "\n"
                + duelo.consultarJugador2().toString();
    }


    public String generarReporteMonstruos() {


        StringBuilder reporte = new StringBuilder();
        reporte.append("MONSTRUOS JUGADOR 1\n");
        for (CartaMonstruo monstruo :
                duelo.consultarMonstruosJugador1()) {


            reporte.append(monstruo.consultarNombre())
                    .append(" | ATQ: ")
                    .append(monstruo.consultarAtaque())
                    .append(" | DEF: ")
                    .append(monstruo.consultarDefensa())
                    .append("\n");
        }

        reporte.append("\nMONSTRUOS JUGADOR 2\n");
        for (CartaMonstruo monstruo :
                duelo.consultarMonstruosJugador2()) {

            reporte.append(monstruo.consultarNombre())
                    .append(" | ATQ: ")
                    .append(monstruo.consultarAtaque())
                    .append(" | DEF: ")
                    .append(monstruo.consultarDefensa())
                    .append("\n");
        }
        return reporte.toString();
    }
}