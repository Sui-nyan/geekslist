package hdm.stuttgart.geekslist;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class TheMovieDb {

    public static TheMovieDb shared = new TheMovieDb();
    private final static String URL = "https://api.themoviedb.org/3/";
    private final static String IMG_URI = "https://image.tmdb.org/t/p";
    private long requestStopTime = 0;

    private HttpClient client;

    //For testing purposes
    public void setHttpClient(HttpClient httpClient) {
        this.client = httpClient;
    }

    Boolean tooManyRequest(HttpResponse<String> response) {
        if (response.statusCode() == HttpStatus.TOO_MANY_REQUESTS.value()) {
            requestStopTime = System.currentTimeMillis() + 60 * 1000; // Stop requests for 1 minute
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        } else {
            return true;
        }
    }

    public HttpResponse<String> getResponse(String pathVariable) {
        String temp = URL + pathVariable;
        try {
            if(requestStopTime > System.currentTimeMillis()) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests", null);
            }
            HttpRequest getRequest = createGetRequest(temp);
            System.out.println("Creating request for url: " + temp);

            return getStringHttpResponse(getRequest);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpResponse<String> getStringHttpResponse(HttpRequest getRequest) throws IOException, InterruptedException {
        HttpClient httpClient = this.client != null ? this.client : HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        if(tooManyRequest(response)) {
            return response;
        } else {
            return null;
        }
    }

    public HttpResponse<String> getResponse(String pathVariable, Map<String, String> queryParameter) {
        URI uriWithParams = buildUri(pathVariable, queryParameter);
        HttpRequest getRequest = createGetRequest(uriWithParams.toString());
        try {
            if(requestStopTime > System.currentTimeMillis()) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests", null);
            }

            return getStringHttpResponse(getRequest);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private URI buildUri(String pathVariable, Map<String, String> queryParameter) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL + pathVariable);
        for (String key: queryParameter.keySet()) {
            builder.queryParam(key, queryParameter.get(key));
        }
        System.out.println("Creating request for url: " + builder.build().toUri());
        return builder.build().toUri();
    }

    private HttpRequest createGetRequest(String url) {
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + Constants.TockenAccess)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String addImageUrl(String image, int width) {
        return String.format("%s/w%d%s", IMG_URI, width, image);
    }

    public int validUrlStatus(String uriString) {
        try {
            URI uri = new URI(uriString);
            HttpURLConnection connection = (HttpURLConnection)uri.toURL().openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            if(responseCode >= 200 && responseCode < 300) {
                return responseCode;
            } else {
                throw new ResponseStatusException(HttpStatus.valueOf(responseCode), uriString);
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error" + e);
            throw new RuntimeException(e);
        }
    }
}
