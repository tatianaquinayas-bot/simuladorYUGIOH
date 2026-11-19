/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

/**
 *
 * @author 57323
 */
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;


import modelo.CartaMonstruo;
import modelo.MonstruoNormal;


public class YGOPRODeckService {


    private static final String URL_BASE =
        "https://db.ygoprodeck.com/api/v7/cardinfo.php?name=";


    private static final HttpClient CLIENTE =
        HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();


    public MonstruoNormal buscarCarta(String nombre)
            throws Exception {


        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Debe ingresar el nombre de una carta."
            );
        }


        String nombreCodificado = URLEncoder.encode(
            nombre.trim(),
            StandardCharsets.UTF_8
        );


        HttpRequest solicitud = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + nombreCodificado))
            .timeout(Duration.ofSeconds(15))
            .GET()
            .build();


        HttpResponse<String> respuesta = CLIENTE.send(
            solicitud,
            HttpResponse.BodyHandlers.ofString()
        );


        if (respuesta.statusCode() != 200) {
            throw new Exception(
                "Error de conexión con YGOPRODeck: "
                + respuesta.statusCode()
            );
        }


        JsonObject json = JsonParser.parseString(
            respuesta.body()
        ).getAsJsonObject();


        if (!json.has("data")) {
            throw new Exception(
                "La API no devolvió información de la carta."
            );
        }


        JsonArray datos = json.getAsJsonArray("data");


        if (datos.isEmpty()) {
            throw new Exception(
                "No se encontró la carta solicitada."
            );
        }


        JsonObject carta = datos.get(0).getAsJsonObject();


        if (!carta.has("atk") || !carta.has("def")) {
            throw new Exception(
                "La carta no tiene estadísticas de monstruo."
            );
        }


        String nombreCarta = carta.get("name").getAsString();


        int ataque = carta.get("atk").getAsInt();
        int defensa = carta.get("def").getAsInt();


        return new MonstruoNormal(
            nombreCarta,
            ataque,
            defensa,
            CartaMonstruo.ATAQUE
        );
    }
}