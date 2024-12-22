package logic;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ScoreService {
    private static final String BASE_URL = "https://devmini.com/score_server.php"; // Replace with your actual server URL

    // GET method to fetch the scores
    public static String getScore() {
        try {
            // Create HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Create HttpRequest (GET request)
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Return the response body (scores)
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching scores.";
        }
    }

    // POST method to send a score
    public static void postScore(String playerName, int score) {
        try {
            // Create HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Prepare the JSON data to send
            String json = String.format("{\"playerName\": \"%s\", \"score\": %d}", playerName, score);

            // Create HttpRequest (POST request)
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json") // Header
                    .POST(HttpRequest.BodyPublishers.ofString(json)) // Body
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}