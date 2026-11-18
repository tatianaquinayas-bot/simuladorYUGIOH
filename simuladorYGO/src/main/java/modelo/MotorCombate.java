/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author 57323
 */
public class MotorCombate {


    public RegistroCombate resolverAtaque(
            CartaMonstruo atacante,
            CartaMonstruo defensor,
            Jugador jugadorAtacante,
            Jugador jugadorDefensor) {

        validar(atacante, jugadorAtacante, jugadorDefensor);

        if (!CartaMonstruo.ATAQUE.equals(
                atacante.consultarPosicion())) {
            throw new IllegalArgumentException(
                "El monstruo atacante debe estar en posición de ataque."
            );}


        if (defensor == null) {
            return resolverAtaque(
                atacante, jugadorAtacante, jugadorDefensor
            );}


        RegistroCombate registro = new RegistroCombate();
        registro.atacante = atacante.consultarNombre();
        registro.defensor = defensor.consultarNombre();
        registro.danio = 0;
        registro.tipoAtaque = "Combate contra monstruo";

        int diferencia;

        if (CartaMonstruo.ATAQUE.equals(
                defensor.consultarPosicion())) {

            diferencia = atacante.consultarAtaque()
                    - defensor.consultarAtaque();

            if (diferencia > 0) {

                registro.resultado =
                    "El defensor fue destruido.";

                registro.danio = diferencia;
                jugadorDefensor.recibirDanio(diferencia);

            } else if (diferencia < 0) {

                registro.resultado =
                    "El atacante fue destruido.";

                registro.danio = Math.abs(diferencia);
                jugadorAtacante.recibirDanio(
                    Math.abs(diferencia)
                );

            } else {

                registro.resultado =
                    "Ambos monstruos fueron destruidos.";
            }

        } else {

            diferencia = atacante.consultarAtaque()
                    - defensor.consultarDefensa();

            if (diferencia > 0) {

                registro.resultado =
                    "El defensor fue destruido.";

            } else if (diferencia < 0) {

                registro.resultado =
                    "El atacante no fue destruido. "
                    + "El jugador atacante recibió daño.";

                registro.danio = Math.abs(diferencia);

                jugadorAtacante.recibirDanio(
                    Math.abs(diferencia)
                );

            } else {

                registro.resultado =
                    "No se destruyó ningún monstruo.";
            }
        }

        return registro;
    }

    // ataque directo sin defensa
    public RegistroCombate resolverAtaque(
            CartaMonstruo atacante,
            Jugador jugadorAtacante,
            Jugador jugadorDefensor) {

        validar(atacante, jugadorAtacante, jugadorDefensor);

        if (!CartaMonstruo.ATAQUE.equals(
                atacante.consultarPosicion())) {
            throw new IllegalArgumentException(
                "El atacante debe estar en posición de ataque."
            );
        }

        RegistroCombate registro = new RegistroCombate();

        registro.atacante = atacante.consultarNombre();
        registro.defensor = "Ataque directo";
        registro.tipoAtaque = "Ataque directo";
        registro.danio = atacante.consultarAtaque();
        registro.resultado =
            "Ataque directo realizado correctamente.";

        jugadorDefensor.recibirDanio(registro.danio);

        return registro;
    }

    public int calcularDanio(int diferencia) {
        return Math.abs(diferencia);
    }

    private void validar(
            CartaMonstruo atacante,
            Jugador jugadorAtacante,
            Jugador jugadorDefensor) {

        if (atacante == null
                || jugadorAtacante == null
                || jugadorDefensor == null) {
            
            

            throw new IllegalArgumentException(
                "Los datos del combate no pueden ser nulos."
            );
        }
    }
}