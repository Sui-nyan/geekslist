package hdm.stuttgart.geekslist.Controller;

import com.google.gson.Gson;
import hdm.stuttgart.geekslist.Models.*;
import hdm.stuttgart.geekslist.TheMovieDb;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@RestController
public class TVController {
    @Cacheable(value = "tv_recommendations")
    @GetMapping("tv/{id}/recommendations")
    public TvResults getRecommendations(@PathVariable String id, @RequestParam(value = "image_width", defaultValue = "500")int width, @RequestParam(value = "language", defaultValue = "en-US")String lang) {
        HttpResponse<String> response = TheMovieDb.shared.getResponse("tv/" + id + "/recommendations", Map.of("language", lang));

        return getTvResults(width, response);
    }

    @Cacheable(value = "tv_similar")
    @GetMapping("tv/{id}/similar")
    public TvResults getSimilarTvShows(@PathVariable String id, @RequestParam(value = "image_width", defaultValue = "500")int width, @RequestParam(value = "language", defaultValue = "en-US")String lang) {
        HttpResponse<String> response = TheMovieDb.shared.getResponse("tv/" + id + "/similar", Map.of("language", lang));

        return getTvResults(width, response);
    }

    @Cacheable(value = "tv_latest")
    @GetMapping("tv/latest")
    public Tv getLatestTvShow(@RequestParam(value = "image_width", defaultValue = "500")int width, @RequestParam(value = "language", defaultValue = "en-US")String lang) {
        HttpResponse<String> response = TheMovieDb.shared.getResponse("tv/latest", Map.of("language", lang));
        Gson gson = new Gson();
        Tv tv = gson.fromJson(response.body(), Tv.class);

        return tv.withImageUrl(TheMovieDb.shared.addImageUrl(tv.posterPath(), width));
    }

    @Cacheable(value = "tv_season")
    @GetMapping("tv/{id}/season/{number}")
    public Season getSeason(@PathVariable int id, @PathVariable int number, @RequestParam(value = "image_width", defaultValue = "500")int width, @RequestParam(value = "language", defaultValue = "en-US")String lang) {
        Gson gson = new Gson();
        HttpResponse<String> response = TheMovieDb.shared.getResponse("tv/" + id + "/season/" + number, Map.of("language", lang));
        Season season = gson.fromJson(response.body(), Season.class);
        season.episodes().replaceAll(e -> e.withImageUrl(TheMovieDb.shared.addImageUrl(e.stillPath(), width)));

        return season.withImageUrl(TheMovieDb.shared.addImageUrl(season.posterPath(), width));
    }

    @Cacheable(value = "tv_episode")
    @GetMapping("tv/{id}/season/{season_number}/episode/{episode_number}")
    public Episode getEpisode(@PathVariable int id, @PathVariable("season_number") int seasonNumber, @PathVariable("episode_number") int episodeNumber, @RequestParam(value = "image_width", defaultValue = "500")int width, @RequestParam(value = "language", defaultValue = "en-US")String lang) {
        Gson gson = new Gson();
        HttpResponse<String> response = TheMovieDb.shared.getResponse("tv/" + id + "/season/" + seasonNumber + "/episode/" + episodeNumber, Map.of("language", lang));

        Episode episode = gson.fromJson(response.body(), Episode.class);

        return episode.withImageUrl(TheMovieDb.shared.addImageUrl(episode.stillPath(), width));
    }

    @Cacheable(value = "tv_seasons")
    @GetMapping("tv/{id}/seasons")
    public List<Season> getSeasons(@PathVariable int id, @RequestParam(value = "image_width", defaultValue = "500")int width, @RequestParam(value = "language", defaultValue = "en-US")String lang) {
        Gson gson = new Gson();
        HttpResponse<String> response = TheMovieDb.shared.getResponse("tv/" + id, Map.of("language", lang));
        Tv tv = gson.fromJson(response.body(), Tv.class);
        tv.seasons().replaceAll(t -> t.withImageUrl(TheMovieDb.shared.addImageUrl(t.posterPath(), width)));

        return tv.seasons();
    }

    @Cacheable(value = "tv_season_watchprovider")
    @GetMapping("/overview/tv/{id}/season/{number}/watchprovider")
    public ProviderCountry getWatchProvidersSeason(@PathVariable int id, @PathVariable int number, @RequestParam String region) {
        HttpResponse<String> response = TheMovieDb.shared.getResponse(String.format("tv/%s/season/%s/watch/providers", id, number));
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

    private TvResults getTvResults(@RequestParam(value = "image_width", defaultValue = "500") int width, HttpResponse<String> response) {
        Gson gson = new Gson();
        TvResults tv = gson.fromJson(response.body(), TvResults.class);
        tv.results().replaceAll(m -> {
            String imageUrl = TheMovieDb.shared.addImageUrl(m.posterPath(), width);
            String backdropUrl = TheMovieDb.shared.addImageUrl(m.backdropPath(), width);
            return m.withImageUrl(imageUrl, backdropUrl);
        });

        return tv;
    }
}
