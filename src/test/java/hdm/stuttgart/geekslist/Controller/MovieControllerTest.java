package hdm.stuttgart.geekslist.Controller;

import hdm.stuttgart.geekslist.Models.Genres;
import hdm.stuttgart.geekslist.Models.Movie;
import hdm.stuttgart.geekslist.TheMovieDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mvc;
    @Mock
    private TheMovieDb theMovieDbMock;
    @Mock
    private HttpResponse<String> mockResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Replace original with mock
        TheMovieDb.shared = theMovieDbMock;

        // Stub methods
        when(theMovieDbMock.getResponse(any())).thenReturn(mockResponse);
        when(theMovieDbMock.getResponse(anyString(), any())).thenReturn(mockResponse);

        // Use original addImageUrl method
        when(theMovieDbMock.addImageUrl(anyString(), anyInt())).thenCallRealMethod();
        when(theMovieDbMock.validUrlStatus(anyString())).thenCallRealMethod();
    }

    @Test
    void testGetRecommendations() throws Exception {
        // Assign
        var responseBody = getJsonString("themoviedb_responses/movie_recommendations.json");
        when(mockResponse.body()).thenReturn(responseBody);

        // Act, Assert
        // TODO adjust assertions
        mvc.perform(MockMvcRequestBuilders.get("/movie/123/recommendations"))
                .andExpect(status().isOk())
                // Verify expected key-value pairs
                .andExpect(jsonPath("$.results[0].adult").value(false))
                .andExpect(jsonPath("$.results[0].id").value(127380))
                .andExpect(jsonPath("$.results[0].title").value("Finding Dory"))
                .andExpect(jsonPath("$.results[0].original_language").value("en"))
                .andExpect(jsonPath("$.results[0].original_title").value("Finding Dory"))
                .andExpect(jsonPath("$.results[0].overview").value("Dory is reunited with her friends Nemo and Marlin in the search for answers about her past. What can she remember? Who are her parents? And where did she learn to speak Whale?"))
                .andExpect(jsonPath("$.results[0].popularity").value(33.659))
                .andExpect(jsonPath("$.results[0].release_date").value("2016-06-16"))
                .andExpect(jsonPath("$.results[0].video").value(false))
                .andExpect(jsonPath("$.results[0].vote_average").value(7.042))
                .andExpect(jsonPath("$.results[0].vote_count").value(11253))
                .andExpect(jsonPath("$.results[0].poster_path").value("https://image.tmdb.org/t/p/w500/3UVe8NL1E2ZdUZ9EDlKGJY5UzE.jpg"))
                .andExpect(jsonPath("$.results[0].backdrop_path").value("https://image.tmdb.org/t/p/w500/8yYuFjRsozwOckhAaTHRLTiDwml.jpg"))
                .andExpect(jsonPath("$.results[0].genre_ids[0]").value(12))
                .andExpect(jsonPath("$.results[0].genre_ids[1]").value(16))
                .andExpect(jsonPath("$.results[0].genre_ids[2]").value(35))
                .andExpect(jsonPath("$.results[0].genre_ids[3]").value(10751))
                .andExpect(jsonPath("$.results[0].media_type").value("movie"));

        // Verify external API is used
        verify(theMovieDbMock).getResponse("movie/123/recommendations");
    }

    @Test
    void testGetSimilarMovies() throws Exception {
        // Assign
        var responseBody = getJsonString("themoviedb_responses/similar_movies.json");
        when(mockResponse.body()).thenReturn(responseBody);

        // Act, Assert
        // TODO adjust assertions
        mvc.perform(MockMvcRequestBuilders.get("/movie/123/similar"))
                .andDo(System.out::println)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].adult").value(false))
                .andExpect(jsonPath("$.results[0].id").value(153))
                .andExpect(jsonPath("$.results[0].original_language").value("en"))
                .andExpect(jsonPath("$.results[0].original_title").value("Lost in Translation"))
                .andExpect(jsonPath("$.results[0].overview").value("Two lost souls visiting Tokyo -- the young, neglected wife of a photographer and a washed-up movie star shooting a TV commercial -- find an odd solace and pensive freedom to be real in each other's company, away from their lives in America."))
                .andExpect(jsonPath("$.results[0].popularity").value(18.59))
                .andExpect(jsonPath("$.results[0].poster_path").value("https://image.tmdb.org/t/p/w500/wkSzJs7oMf8MIr9CQVICsvRfwA7.jpg"))
                .andExpect(jsonPath("$.results[0].backdrop_path").value("https://image.tmdb.org/t/p/w500/45He7gApNQyDbqCDjxew9BnHzjf.jpg"))
                .andExpect(jsonPath("$.results[0].release_date").value("2003-09-18"))
                .andExpect(jsonPath("$.results[0].title").value("Lost in Translation"))
                .andExpect(jsonPath("$.results[0].video").value(false))
                .andExpect(jsonPath("$.results[0].vote_average").value(7.395))
                .andExpect(jsonPath("$.results[0].vote_count").value(6559))
                .andExpect(jsonPath("$.results[0].genre_ids[0]").value(18))
                .andExpect(jsonPath("$.results[0].genre_ids[1]").value(10749))
                .andExpect(jsonPath("$.results[0].genre_ids[2]").value(35))
                .andReturn();

        verify(theMovieDbMock).getResponse("movie/123/similar");
    }

    @Test
    void testGetLatestMovie() throws Exception {
        // Assign
        var responseBody = getJsonString("themoviedb_responses/latest_movie.json");
        when(mockResponse.body()).thenReturn(responseBody);

        // Act, Assert
        // TODO adjust assertions
        mvc.perform(MockMvcRequestBuilders.get("/movie/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adult").value(false))
                .andExpect(jsonPath("$.backdrop_path").isEmpty())
                .andExpect(jsonPath("$.budget").value(0))
                .andExpect(jsonPath("$.genres").isEmpty())
                .andExpect(jsonPath("$.homepage").value("https://findingfellowship.film/"))
                .andExpect(jsonPath("$.id").value(1141892))
                .andExpect(jsonPath("$.original_language").value("en"))
                .andExpect(jsonPath("$.original_title").value("Finding Fellowship"))
                .andExpect(jsonPath("$.overview").value("The title Finding Fellowship is a double entendre. Fellowship is the name of the lane that two of the film’s subjects, Kisha and Jason, grew up on. This documentary has been about actively unearthing and finding the history of the community in which they grew up and the street that they grew up on is a wonderful symbol of that community. But the film is also about the power of people coming together in a shared interest and how that can only be achieved when actively pursued. In a world where we are often told that we are irreconcilably divided, we still believe in finding fellowship, and it’s this story that gives us hope."))
                .andExpect(jsonPath("$.poster_path").isNotEmpty())
                .andExpect(jsonPath("$.release_date").isEmpty())
                .andExpect(jsonPath("$.revenue").value(0))
                .andExpect(jsonPath("$.runtime").value(57))
//                .andExpect(jsonPath("$.spoken_languages[0].english_name").value("English"))
//                .andExpect(jsonPath("$.spoken_languages[0].iso_639_1").value("en"))
//                .andExpect(jsonPath("$.spoken_languages[0].name").value("English"))
                .andExpect(jsonPath("$.status").value("Released"))
//                .andExpect(jsonPath("$.tagline").isEmpty())
                .andExpect(jsonPath("$.title").value("Finding Fellowship"));
//                .andExpect(jsonPath("$.video").value(false))

        verify(theMovieDbMock).getResponse("movie/latest");
    }

    @Test
    void testGetNowPlayingMovies() throws Exception {
        // Assign
        var responseBody = getJsonString("themoviedb_responses/now_playing_movie_regionUS.json");
        when(mockResponse.body()).thenReturn(responseBody);

        // Act, Assert
        // TODO adjust assertions
        mvc.perform(MockMvcRequestBuilders.get("/movie/now_playing?page=1&region=US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dates.maximum").value("2023-06-23"))
                .andExpect(jsonPath("$.dates.minimum").value("2023-05-06"))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.results[0].adult").value(false))
                .andExpect(jsonPath("$.results[0].backdrop_path").value("/gAo47pvBbcPGvNjjadA65WImQ6X.jpg"))
                .andExpect(jsonPath("$.results[0].genre_ids[0]").value(28))
                .andExpect(jsonPath("$.results[0].genre_ids[1]").value(80))
                .andExpect(jsonPath("$.results[0].genre_ids[2]").value(53))
                .andExpect(jsonPath("$.results[0].id").value(385687))
                .andExpect(jsonPath("$.results[0].original_language").value("en"))
                .andExpect(jsonPath("$.results[0].original_title").value("Fast X"))
                .andExpect(jsonPath("$.results[0].overview").value("Over many missions and against impossible odds, Dom Toretto and his family have outsmarted, out-nerved and outdriven every foe in their path. Now, they confront the most lethal opponent they've ever faced: A terrifying threat emerging from the shadows of the past who's fueled by blood revenge, and who is determined to shatter this family and destroy everything—and everyone—that Dom loves, forever."))
                .andExpect(jsonPath("$.results[0].popularity").value(9867.49))
                .andExpect(jsonPath("$.results[0].poster_path").value("/fiVW06jE7z9YnO4trhaMEdclSiC.jpg"))
                .andExpect(jsonPath("$.results[0].release_date").value("2023-05-19"))
                .andExpect(jsonPath("$.results[0].title").value("Fast X"))
                .andExpect(jsonPath("$.total_pages").value(14))
                .andExpect(jsonPath("$.total_results").value(268));

        verify(theMovieDbMock).getResponse("movie/now_playing", Map.of("page", "1", "region", "US"));
    }

    private String getJsonString(String fileName) throws IOException {
        try (var resource = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (null == resource) {
                throw new RuntimeException("File could not be loaded. Check if file exists.");
            }
            return new String(resource.readAllBytes());
        }
    }

    // TODO remove if not needed
    private Movie createTestMovie() {
        List<Genres> genres = new ArrayList<>();
        genres.add(new Genres(12, "Adventure"));
        genres.add(new Genres(16, "Animation"));
        genres.add(new Genres(14, "Fantasy"));

        return Movie.builder()
                .originalTitle("The Lord of the Rings")
                .originalTitle("/liW0mjvTyLs7UCumaHhx3PpU4VT.jpg")
                .title("The Fellowship of the Ring embark on a journey to destroy the One Ring and end Sauron\u0027s reign over Middle-earth.")
                .posterPath("/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg")
                .releaseDate("1978-11-15")
                .runtime(132)
                .budget(4000000)
                .revenue(30471420)
                .genres(genres)
                .build();
    }

    @Test
    void testGetRecommendationsWithNegativeId() throws Exception {
        // Assign, Act, Assert
        mvc.perform(MockMvcRequestBuilders.get("/movie/-1/recommendations"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetSimilarMoviesWithNegativeId() throws Exception {
        // Assign, Act, Assert
        mvc.perform(MockMvcRequestBuilders.get("/movie/-1/similar"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetNowPlayingWithNegativePage() throws Exception {
        // Assign, Act, Assert
        mvc.perform(MockMvcRequestBuilders.get("/movie/now_playing?page=-1&region=US"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Should throw exception when get now playing with unknown region")
    void testGetNowPlayingWithUnknownRegion() throws Exception {
        // Assign, Act, Assert
        when(mockResponse.statusCode()).thenReturn(404);
        mvc.perform(MockMvcRequestBuilders.get("/movie/now_playing?page=1&region=TEYVAT"))
                .andExpect(status().is4xxClientError());
    }
}
