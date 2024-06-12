package hdm.stuttgart.geekslist.Controller;

import com.google.gson.Gson;
import hdm.stuttgart.geekslist.Models.*;
import hdm.stuttgart.geekslist.TheMovieDb;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/overview")
public class OverviewController {

    @Cacheable("overview")
    @GetMapping("/{type}/{id}")
    public Record getOverviewDetails(@PathVariable int id, @PathVariable String type, @RequestParam(value = "image_width", defaultValue = "500")int imageWidth, @RequestParam(value = "language", defaultValue = "en-US")String lang, @RequestParam(value = "backdrop_image_width", defaultValue = "500")int backdropWidth) {
        Gson gson = new Gson();
        String path = type + "/" + id + Map.of("language", lang);
        HttpResponse<String> Response = TheMovieDb.shared.getResponse(type + "/" + id, Map.of("language", lang));

        
        if (type.equalsIgnoreCase("movie")) {

            Movie movie = gson.fromJson(Response.body(), Movie.class);

            String moviePoster = TheMovieDb.shared.addImageUrl(movie.posterPath(), imageWidth);
            String movieBackdrop = TheMovieDb.shared.addImageUrl(movie.backdropPath(), backdropWidth);
            TheMovieDb.shared.validUrlStatus(moviePoster);

            return movie.withImageUrl(moviePoster, movieBackdrop);

        } else if (type.equalsIgnoreCase("tv")) {

            Tv tv = gson.fromJson(Response.body(), Tv.class);

            String tvPoster = TheMovieDb.shared.addImageUrl(tv.posterPath(), imageWidth);
            String tvBackdrop = TheMovieDb.shared.addImageUrl(tv.backdropPath(), backdropWidth);
            TheMovieDb.shared.validUrlStatus(tvPoster);
            List<Season> seasons = new ArrayList<>(tv.seasons());
            for (int i = 0; i < seasons.size(); i++) {
                Season season = seasons.get(i);
                if(season.posterPath() == null) {
                    continue;
                }
                String seasonPoster = TheMovieDb.shared.addImageUrl(season.posterPath(), imageWidth);
                System.out.println(TheMovieDb.shared.validUrlStatus(seasonPoster));
                seasons.set(i, season.withImageUrl(seasonPoster));
            }
            Tv editedSeasons = tv.editSeasons(seasons);

            return editedSeasons.withImageUrl(tvPoster, tvBackdrop);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Such Media");
    }

    @GetMapping("/{type}/{id}/actors")
    public Credits getActors(@PathVariable int id, @PathVariable String type) {
        HttpResponse<String> response = TheMovieDb.shared.getResponse(type + "/" + id + "/credits");
        Gson gson = new Gson();
        Credits credits = gson.fromJson(response.body(), Credits.class);

        credits.cast().removeIf(c -> !c.department().equalsIgnoreCase("Acting"));

        return credits;
    }

    @GetMapping("/{type}/{id}/crew")
    public Credits getCrew(@PathVariable int id, @PathVariable String type, @RequestParam(value = "image_width", defaultValue = "500") int imageWidth) {
        HttpResponse<String> response = TheMovieDb.shared.getResponse(type + "/" + id + "/credits");
        Gson gson = new Gson();
        Credits credits = gson.fromJson(response.body(), Credits.class);

        credits.cast().removeIf(c -> c.department().equalsIgnoreCase("Acting"));
        credits.cast().replaceAll(c -> c.withImageUrl(TheMovieDb.shared.addImageUrl(c.profilePath(), imageWidth)));

        return credits;
    }

    @Cacheable("watchprovider")
    @GetMapping("/{type}/{id}/watchprovider")
    public ProviderCountry getWatchProviders(@PathVariable int id, @PathVariable String type, @RequestParam String region) {
        if(checkType(type)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Such Media");
        }
        HttpResponse<String> response = TheMovieDb.shared.getResponse(type + "/" + id + "/watch/providers");
        Gson gson = new Gson();
        WatchProvider watchProvider = gson.fromJson(response.body(), WatchProvider.class);

        if(watchProvider == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Data");
        }
        if (!watchProvider.results().containsKey(region.toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Region Not Available");
        }

        return watchProvider.results().get(region);
    }

    private boolean checkType(String type) {
        if (type.equalsIgnoreCase("movie")) {
            return true;
        } else return type.equalsIgnoreCase("tv");
    }
}
