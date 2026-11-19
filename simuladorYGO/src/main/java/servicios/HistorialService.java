/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

/**
 *
 * @author 57323
 */
import modelo.RegistroCombate;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistorialService {

    private static final String ARCHIVO =
            "historial_combate.txt";

    public void guardar(RegistroCombate registro)
            throws IOException {

        if (registro == null) {
            throw new IllegalArgumentException(
                "El registro no puede ser nulo."
            );
        }

        try (BufferedWriter escritor =
                new BufferedWriter(
                    new FileWriter(ARCHIVO, true))) {

            escritor.write(
                "===================================="
            );
            escritor.newLine();
            escritor.write(
                LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm:ss"
                    )
                )
            );

            escritor.newLine();
            escritor.write(registro.generarReporte());
            escritor.newLine();
        }
    }
}