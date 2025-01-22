package com.dailycodework.concurrancy;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    // Fetch exchange rate data
    public static String fetchExchangeRates(String baseCurrency) throws Exception {
        String url = API_URL + baseCurrency;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); // Return JSON response as a String
    }

    // Extract exchange rate from JSON response
    public static double getExchangeRate(String jsonResponse, String targetCurrency) {
        int targetIndex = jsonResponse.indexOf("\"" + targetCurrency + "\":");
        if (targetIndex == -1) {
            throw new IllegalArgumentException("Target currency not found.");
        }
        int startIndex = targetIndex + targetCurrency.length() + 3; // Adjust for "currency":
        int endIndex = jsonResponse.indexOf(",", startIndex);
        if (endIndex == -1) { // If it's the last value
            endIndex = jsonResponse.indexOf("}", startIndex);
        }
        return Double.parseDouble(jsonResponse.substring(startIndex, endIndex));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Step 1: User selects base currency
            System.out.print("Enter base currency (e.g., USD, EUR): ");
            String baseCurrency = scanner.nextLine().toUpperCase();

            // Step 2: User selects target currency
            System.out.print("Enter target currency (e.g., INR, GBP): ");
            String targetCurrency = scanner.nextLine().toUpperCase();

            // Step 3: Fetch exchange rates
            String jsonResponse = fetchExchangeRates(baseCurrency);

            // Step 4: Extract exchange rate for the target currency
            double rate = getExchangeRate(jsonResponse, targetCurrency);

            // Step 5: User inputs amount to convert
            System.out.print("Enter amount to convert: ");
            double amount = scanner.nextDouble();

            // Step 6: Perform conversion and display result
            double convertedAmount = amount * rate;
            System.out.printf("Converted Amount: %.2f %s%n", convertedAmount, targetCurrency);

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
