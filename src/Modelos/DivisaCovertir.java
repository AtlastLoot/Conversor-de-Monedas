package Modelos;

import Registros.Registro;
import com.google.gson.Gson; // Librería para convertir JSON a objetos Java
import com.google.gson.JsonSyntaxException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient; // Cliente HTTP para realizar solicitudes
import java.net.http.HttpRequest; // Solicitudes HTTP
import java.net.http.HttpResponse; // Respuestas HTTP
import java.util.HashMap;
import java.util.Map;

public class DivisaCovertir {
    private final Map<String, ConversorRates> conversionRates = new HashMap<>(); // Mapa para almacenar las tasas de conversión
    private final HttpClient httpClient = HttpClient.newHttpClient(); // Crear un cliente HTTP
    private final Gson gson = new Gson(); // Crear un objeto Gson para manejar JSON
    Registro registro = new Registro();


    // Metodo para obtener tasas de conversión desde la API
    public void fetchConversionRates(String apiUrl) {
        try {
            // Crear una solicitud HTTP GET
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl)) // Establecer la URL de la solicitud
                    .GET()
                    .build();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar si la respuesta fue exitosa
            if (response.statusCode() == 200) {
                parseResponse(response.body()); // Procesar la respuesta JSON
            } else {
                System.out.println("Error: " + response.statusCode()); // Imprimir error si la solicitud falla
            }
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir cualquier excepción que ocurra
        }
    }

    // Metodo para procesar la respuesta JSON de la API
    private void parseResponse(String jsonResponse) {
        try {
            // Convertir la respuesta JSON a un objeto Modelos.ApiResponse
            ApiResponse apiResponse = gson.fromJson(jsonResponse, ApiResponse.class);
            // Iterar sobre las tasas de conversión y agregarlas al mapa
            for (Map.Entry<String, Double> entry : apiResponse.getConversionRates().entrySet()) {
                String currency = entry.getKey(); // Obtener el código de la moneda
                double rate = entry.getValue(); // Obtener la tasa de conversión
                conversionRates.put(currency, new ConversorRates(currency, rate)); // Guardar en el mapa
            }
        } catch (JsonSyntaxException e) {
            System.out.println("Error al parsear la respuesta JSON: " + e.getMessage()); // Manejo de errores al parsear JSON
        }
    }

    // metodo para convertir una cantidad de una moneda a otra
    public double convert(String fromCurrency, String toCurrency, double amount) {
        // Verificar que ambas monedas estén disponibles
        if (conversionRates.containsKey(fromCurrency) && conversionRates.containsKey(toCurrency)) {
            double fromRate = conversionRates.get(fromCurrency).rate(); // Obtener la tasa de la moneda de origen
            double toRate = conversionRates.get(toCurrency).rate(); // Obtener la tasa de la moneda de destino


            double convertedAmount = amount * (toRate / fromRate);
            registro.saveToHistory(fromCurrency, toCurrency, amount, convertedAmount);
            // Calcular y devolver el monto convertido
            return convertedAmount;
        } else {
            throw new IllegalArgumentException("Moneda no soportada"); // Lanzar excepción si la moneda no es válida
        }
    }

    // Getter para obtener todas las tasas de conversión
    public Map<String, ConversorRates> getConversionRates() {
        return conversionRates;
    }


}
