package hdm.stuttgart.geekslist.integration;

import hdm.stuttgart.geekslist.TheMovieDb;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TheMovieDbTest {
    private static final String MOVIE_PATH = "movie/";


    private final TheMovieDb sut = TheMovieDb.shared;

    @Test
    void testGetResponse() {
        // Assign
        var path = MOVIE_PATH + "550";

        // Act
        var response = sut.getResponse(path);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.statusCode());

        var responseBody = response.body();
        assertNotEquals(0, responseBody.length());
        assertTrue(responseBody.contains("Fight Club"));
    }

    @Test
    void testGetResponseWithQueryParameters() {
        // Assign
        var path = MOVIE_PATH + 277834;
        var queryParameters = Map.of("language", "DE");

        // Act
        var response = sut.getResponse(path, queryParameters);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.statusCode());

        var responseBody = response.body();
        assertNotEquals(0, responseBody.length());
        assertTrue(responseBody.contains("Moana"));
        assertTrue(responseBody.contains("Vaiana"));
    }

    @Test
    void testAddImageUrl() {
        // Assign
        var image = "/example-image.jpg";
        var resultingUrl = "https://image.tmdb.org/t/p/w500" + image;
        var width = 500;

        // Act
        var imageUrl = sut.addImageUrl(image, width);

        // Assert
        assertEquals(resultingUrl, imageUrl);
    }

    @Test
    void testValidUrlStatus() {
        String validUrl = "https://example.com"; // Assuming a valid URL for testing
        int statusCode = sut.validUrlStatus(validUrl);

        assertTrue(statusCode >= 200 && statusCode < 300); // Assert that the URL returns a successful status code
    }

    @Test
    void testInvalidUrlStatus() {
        String invalidUrl = "https://api.themoviedb.org/3/movie/550"; // Assuming an invalid URL for testing. Unauthorized
        assertThrows(ResponseStatusException.class, () -> sut.validUrlStatus(invalidUrl));
    }

    @Test
    void localTest() {
        System.out.println(sut.getResponse("movie/now_playing?page=1&region=TEYVAT").body());
    }
}
