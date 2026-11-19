/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author 57323
 */
import java.util.ArrayList;
import java.util.List;

public class Duelo {


    private Jugador jugador1 = new Jugador();
    private Jugador jugador2 = new Jugador();
    private ArrayList<CartaMonstruo> monstruosJugador1 =
            new ArrayList<>();
    private ArrayList<CartaMonstruo> monstruosJugador2 =
            new ArrayList<>();
    private ArrayList<RegistroCombate> historial =
            new ArrayList<>();
    private MotorCombate motor = new MotorCombate();


    public void configurarJugadores(String nombre1, String nombre2) {


        jugador1.setNombre(nombre1);
        jugador2.setNombre(nombre2);
        jugador1.setPuntosVida(Jugador.LP_INICIALES);
        jugador2.setPuntosVida(Jugador.LP_INICIALES);
    }

    public void registrarMonstruo(
            CartaMonstruo monstruo, int numeroJugador) {

        if (monstruo == null) {
            throw new IllegalArgumentException(
                "El monstruo no puede ser nulo."
            );
        }

        if (numeroJugador == 1) {
            monstruosJugador1.add(monstruo);

        } else if (numeroJugador == 2) {
            monstruosJugador2.add(monstruo);

        } else {
            throw new IllegalArgumentException(
                "El jugador debe ser 1 o 2."
            );
        }
    }

    public RegistroCombate atacar(
            CartaMonstruo atacante,
            CartaMonstruo defensor,
            int numeroJugador) {


        RegistroCombate registro;

        if (numeroJugador == 1) {

            registro = motor.resolverAtaque(
                atacante, defensor, jugador1, jugador2
            );

        } else if (numeroJugador == 2) {


            registro = motor.resolverAtaque(
                atacante, defensor, jugador2, jugador1
            );


        } else {


            throw new IllegalArgumentException(
                "Jugador inválido."
            );
        }


        eliminarDestruidos(
            atacante, defensor, numeroJugador, registro
        );


        historial.add(registro);


        return registro;
    }


    public RegistroCombate atacarDirectamente(
            CartaMonstruo atacante,
            int numeroJugador) {


        ArrayList<CartaMonstruo> defensores =
                numeroJugador == 1
                ? monstruosJugador2
                : monstruosJugador1;


        if (!defensores.isEmpty()) {
            throw new IllegalStateException(
                "No se puede atacar directamente: "
                + "el rival tiene monstruos en el campo."
            );
        }


        RegistroCombate registro;


        if (numeroJugador == 1) {


            registro = motor.resolverAtaque(
                atacante, jugador1, jugador2
            );


        } else if (numeroJugador == 2) {


            registro = motor.resolverAtaque(
                atacante, jugador2, jugador1
            );


        } else {


            throw new IllegalArgumentException(
                "Jugador inválido."
            );
        }


        historial.add(registro);


        return registro;
    }


    private void eliminarDestruidos(
            CartaMonstruo atacante,
            CartaMonstruo defensor,
            int numeroJugador,
            RegistroCombate registro) {


        if (numeroJugador == 1) {


            if (registro.resultado.contains(
                    "El defensor fue destruido")
                    || registro.resultado.contains(
                    "Ambos monstruos fueron destruidos")) {


                monstruosJugador2.remove(defensor);
            }


            if (registro.resultado.contains(
                    "El atacante fue destruido")) {


                monstruosJugador1.remove(atacante);
            }


        } else {


            if (registro.resultado.contains(
                    "El defensor fue destruido")
                    || registro.resultado.contains(
                    "Ambos monstruos fueron destruidos")) {


                monstruosJugador1.remove(defensor);
            }


            if (registro.resultado.contains(
                    "El atacante fue destruido")) {


                monstruosJugador2.remove(atacante);
            }
        }
    }


    public Jugador consultarJugador1() {
        return jugador1;
    }


    public Jugador consultarJugador2() {
        return jugador2;
    }


    public List<CartaMonstruo> consultarMonstruosJugador1() {
        return monstruosJugador1;
    }


    public List<CartaMonstruo> consultarMonstruosJugador2() {
        return monstruosJugador2;
    }


    public List<RegistroCombate> consultarHistorial() {
        return historial;
    }


    public boolean hayGanador() {
        return jugador1.estaDerrotado()
                || jugador2.estaDerrotado();
    }


    public String consultarGanador() {


        if (jugador1.estaDerrotado()) {
            return jugador2.getNombre();
        }


        if (jugador2.estaDerrotado()) {
            return jugador1.getNombre();
        }


        return "El duelo continúa";
    }
}
