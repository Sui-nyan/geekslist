package hdm.stuttgart.geekslist.Controller;

import com.google.gson.Gson;
import hdm.stuttgart.geekslist.Models.MovieResults;
import hdm.stuttgart.geekslist.Models.TvResults;
import hdm.stuttgart.geekslist.TheMovieDb;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.Map;

@RestController
public class SearchController {
    public String encodeQuery(String query) {
        return query.replaceAll(" ", "%20");
    }

    @GetMapping("search/movie")
    public String searchMovies(@RequestParam(value = "Language", defaultValue = "en_US" )String language, @RequestParam String query, @RequestParam(value = "adult", defaultValue = "false") Boolean adult, @RequestParam int page) {
        HttpResponse<String> result = TheMovieDb.shared.getResponse("search/movie", Map.of("query", encodeQuery(query), "include_adult", adult.toString(), "language", language, "page", String.valueOf(page)));
        Gson gson = new Gson();
        MovieResults movieResults = gson.fromJson(result.body(), MovieResults.class);
        return gson.toJson(movieResults);
    }

    @GetMapping("search/tv")
    public String searchTv(@RequestParam(value = "Language", defaultValue = "en_US" )String language, @RequestParam String query, @RequestParam(value = "adult", defaultValue = "false") Boolean adult, @RequestParam int page) {
        Map<String, String> queryParameter = Map.of("query", encodeQuery(query), "include_adult", adult.toString(), "language", language, "page", String.valueOf(page));
        HttpResponse<String> result = TheMovieDb.shared.getResponse("search/tv", queryParameter);
        Gson gson = new Gson();
        TvResults tvResults = gson.fromJson(result.body(), TvResults.class);

        return gson.toJson(tvResults);
    }
}
