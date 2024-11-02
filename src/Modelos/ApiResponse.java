package Modelos;

import java.util.Map;
// Clase para representar la respuesta de la API
public class ApiResponse {
    private Map<String, Double> conversion_rates;
    // Getter para obtener las tasas de conversión
    public Map<String, Double> getConversionRates() {
        return conversion_rates;
    }
}
