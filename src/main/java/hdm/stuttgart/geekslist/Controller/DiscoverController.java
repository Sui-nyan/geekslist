package hdm.stuttgart.geekslist.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hdm.stuttgart.geekslist.Models.MovieResults;
import hdm.stuttgart.geekslist.Models.Region;
import hdm.stuttgart.geekslist.Models.Results;
import hdm.stuttgart.geekslist.Models.TvResults;
import hdm.stuttgart.geekslist.TheMovieDb;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
import java.util.Map;

@RestController
public class DiscoverController {
    @Cacheable(value = "discover")
    @GetMapping("/discover/{type}")
    public Record getDiscoverPage(@RequestParam int page, @PathVariable String type,
                                  @RequestParam(value = "image_width", defaultValue = "500") int imageWidth,
                                  @RequestParam(value = "include_adult", defaultValue = "false") boolean includeAdult,
                                  @RequestParam(value = "region", defaultValue = "US") String region,
                                  @RequestParam(value = "language", defaultValue = "en_US") String language) {
        HttpResponse<String> response = TheMovieDb.shared.getResponse("discover/" + type.toLowerCase(), Map.of("page", String.valueOf(page), "include_adult", String.valueOf(includeAdult), "language", language, "region", region));
        Gson gson = new Gson();
        if (type.equalsIgnoreCase("tv")) {
            TvResults discover = gson.fromJson(response.body(), TvResults.class);
            if (discover.results() == null || discover.results().size() < 1)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page is empty");
            discover.results().replaceAll(t -> t.withImageUrl(TheMovieDb.shared.addImageUrl(t.posterPath(), imageWidth)));
            return discover;
        } else if (type.equalsIgnoreCase("movie")) {
            MovieResults discover = gson.fromJson(response.body(), MovieResults.class);
            if (discover.results() == null || discover.results().size() < 1)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page is empty");
            discover.results().replaceAll(t -> t.withImageUrl(TheMovieDb.shared.addImageUrl(t.posterPath(), imageWidth)));
            return discover;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Such Media");
    }

    @Cacheable(value = "regions")
    @GetMapping("/regions")
    public Results<Region> getRegions(@RequestParam(value = "language", defaultValue = "en-US") String language) {
        var response = TheMovieDb.shared.getResponse("/watch/providers/regions", Map.of("language", language));
        Gson gson = new Gson();
        return gson.fromJson(response.body(), new TypeToken<Results<Region>>(){}.getType());
    }
}
