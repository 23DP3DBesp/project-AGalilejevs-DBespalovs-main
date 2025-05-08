package lv.rvt;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RandomWordGenerator {
    private static final String API_URL = "https://random-word-api.vercel.app/api?words=1&length=5"; // API URL, lai iegūtu nejaušu vārdu
    private static final HttpClient client = HttpClient.newHttpClient(); // HTTP klients, lai nosūtītu pieprasījumus

    // Funkcija, lai iegūtu nejaušu vārdu no API
    public String getWord() throws IOException, InterruptedException {
        // Izveido HTTP pieprasījumu uz API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL)) // URL, uz kuru tiek nosūtīts pieprasījums
                .build();

        // Nosūta pieprasījumu un sagaida atbildi
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Pārbauda, vai atbilde ir veiksmīga
        if (response.statusCode() != 200) {
            throw new IOException("API request failed with status: " + response.statusCode()); // Ja kļūda, izmest izņēmumu
        }

        // Atgriež iegūto vārdu, noņem liekās zīmes (kā [ ] un " )
        return response.body()
                .replace("[", "")  // Noņem sākuma kvadrātiekavas
                .replace("]", "")  // Noņem beigu kvadrātiekavas
                .replace("\"", "") // Noņem pēdiņas
                .trim(); // Noņem liekās atstarpes
    }
}
