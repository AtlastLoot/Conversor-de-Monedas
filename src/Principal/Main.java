package Principal;

import Modelos.DivisaCovertir;
import Registros.Registro;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        DivisaCovertir converter = new DivisaCovertir(); // Crear una instancia del convertidor de monedas
        Registro registro = new Registro();
        String apiUrl = "https://v6.exchangerate-api.com/v6/f4495ab7cf55ef5a6ff760d9/latest/USD"; // URL de la API
        converter.fetchConversionRates(apiUrl); // Obtener tasas de conversión desde la API

        Scanner scanner = new Scanner(System.in); // Crear un escáner para la entrada del usuario
        while (true){
            // Solicitar la moneda de origen al usuario
            System.out.print("Ingrese la moneda de origen (ej. USD) o 'salir' para terminar: : ");
            String fromCurrency = scanner.nextLine().toUpperCase();
            if (fromCurrency.equals("SALIR")) {
                break; // Salir del bucle si el usuario escribe "salir"
            }

            // Solicitar la moneda de destino al usuario
            System.out.print("Ingrese la moneda de destino (ej. EUR): ");
            String toCurrency = scanner.nextLine().toUpperCase();

            // Solicitar la cantidad a convertir
            System.out.print("Ingrese la cantidad a convertir: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            try {
                // Realizar la conversión y mostrar el resultado
                double convertedAmount = converter.convert(fromCurrency, toCurrency, amount);
                //registro.printHistory();
                System.out.printf("%.2f %s equivalen a %.2f %s%n", amount, fromCurrency, convertedAmount, toCurrency);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage()); // Mostrar mensaje de error si la moneda no es válida
            }

            // Opción para imprimir el historial
            System.out.println("¿Desea ver el historial de conversiones? (sí/no): ");
            String showHistory = scanner.nextLine().toLowerCase();
            if (showHistory.equals("si")) {
                registro.printHistory(); // Mostrar el historial de conversiones
            }
        }

        scanner.close(); // Cerrar el escáner

    }
}