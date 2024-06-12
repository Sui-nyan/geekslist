package hdm.stuttgart.geekslist.Controller;

import com.google.gson.Gson;
import hdm.stuttgart.geekslist.Models.Movie;
import hdm.stuttgart.geekslist.Models.MovieResults;
import org.springframework.cache.annotation.Cacheable;
import hdm.stuttgart.geekslist.TheMovieDb;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.Map;

@RestController
public class MovieController {
    @Cacheable(value = "movie_recommendations")
    @GetMapping("movie/{id}/recommendations")
    public MovieResults getRecommendations(@PathVariable String id, @RequestParam(value = "image_width", defaultValue = "500")int width) {
        HttpResponse<String> response = TheMovieDb.shared.getResponse("movie/" + id + "/recommendations");
        return getMovieResults(width, response);
    }

    @Cacheable(value = "movie_similar")
    @GetMapping("movie/{id}/similar")
    public MovieResults getSimilarMovies(@PathVariable String id, @RequestParam(value = "image_width", defaultValue = "500")int width) {
        HttpResponse<String> response = TheMovieDb.shared.getResponse("movie/" + id + "/similar");
        return getMovieResults(width, response);
    }

    @Cacheable(value = "movie_latest")
    @GetMapping("movie/latest")
    public Movie getLatestMovie(@RequestParam(value = "image_width", defaultValue = "500")int width) {
        HttpResponse<String> response = TheMovieDb.shared.getResponse("movie/latest");
        Gson gson = new Gson();
        Movie movie = gson.fromJson(response.body(), Movie.class);

        return movie.withImageUrl(TheMovieDb.shared.addImageUrl(movie.posterPath(), width), TheMovieDb.shared.addImageUrl(movie.backdropPath(), width));
    }

    @Cacheable(value = "movie_now_playing")
    @GetMapping("movie/now_playing")
    public MovieResults getNowPlayingMovies(@RequestParam(value = "image_width", defaultValue = "500")int width, @RequestParam(value = "page")int page, @RequestParam(value = "region", defaultValue = "US")String region, @RequestParam(value = "language", defaultValue = "en_US")String language) {
        HttpResponse<String> response = TheMovieDb.shared.getResponse("movie/now_playing", Map.of("page", String.valueOf(page), "region", region, "language", language));

        return getMovieResults(width, response);
    }

    private MovieResults getMovieResults(@RequestParam(value = "image_width", defaultValue = "500") int width, HttpResponse<String> response) {
        Gson gson = new Gson();
        MovieResults movies = gson.fromJson(response.body(), MovieResults.class);
        movies.results().replaceAll(m -> {
            String imageUrl = TheMovieDb.shared.addImageUrl(m.posterPath(), width);
            String backdropUrl = TheMovieDb.shared.addImageUrl(m.backdropPath(), width);
            return m.withImageUrl(imageUrl, backdropUrl);
        });

        return movies;
    }
}
