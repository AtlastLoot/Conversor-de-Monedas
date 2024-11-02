package Registros;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Registro {
    private final String historyFile = "conversion_history.txt";
    public void saveToHistory(String fromCurrency, String toCurrency, double amount, double convertedAmount) {
        try (FileWriter fw = new FileWriter(historyFile, true);
             PrintWriter writer = new PrintWriter(fw)) {
            LocalDateTime now = LocalDateTime.now(); // Obtener la fecha y hora actual
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            writer.printf("%s: %.2f %s -> %.2f %s%n",now.format(formatter), amount, fromCurrency, convertedAmount, toCurrency);
        } catch (IOException e) {
            System.out.println("Error al guardar el historial de conversiones: " + e.getMessage());
        }
    }

    public void printHistory() {
        System.out.println("Historial de conversiones:");
        try {
            java.nio.file.Files.lines(java.nio.file.Paths.get(historyFile))
                    .forEach(System.out::println); // Leer y mostrar el contenido del archivo
        } catch (IOException e) {
            System.out.println("Error al leer el historial de conversiones: " + e.getMessage());
        }
    }
}
