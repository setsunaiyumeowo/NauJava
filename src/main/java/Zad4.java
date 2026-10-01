import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Zad4 {
    public static void main() throws IOException, InterruptedException {
        String url = "https://httpbin.org/anything";
        try (HttpClient client = HttpClient.newHttpClient()){
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json, text/plain, */*")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
            String jsonText = response.body();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(jsonText);
            JsonNode headers = json.path("headers");
            JsonNode acceptNode = headers.has("Accept") ? headers.get("Accept") : headers.get("accept");
            String acceptHeader = (acceptNode != null) ? acceptNode.asText() : "не найден";
            System.out.println("Допустимые типы ответа (поле `Accept`): " + acceptHeader);
        }
    }
}
