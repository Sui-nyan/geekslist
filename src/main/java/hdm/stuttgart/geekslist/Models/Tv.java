package hdm.stuttgart.geekslist.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record Tv(Boolean adult,
                 @SerializedName("original_name") String originalName,
                 String name,
                 @SerializedName("poster_path") String posterPath,
                 @SerializedName("backdrop_path") String backdropPath,
                 @SerializedName("created_by") List<Creators> createdBy,
                 String overview,
                 String homepage,
                 @SerializedName("original_language")
                 String originalLanguage,
                 int id,
                 @SerializedName("in_production")
                 Boolean inProduction,
                 @SerializedName("last_air_date")
                 String lastAirDate,
                 @SerializedName("first_air_date")
                 String firstAirDate,
                 @SerializedName("number_of_episodes")
                 int numberOfEpisodes,
                 @SerializedName("number_of_seasons")
                 int numberOfSeasons,
                 @SerializedName("release_date") String releaseDate,
                 String status,
                 List<Season> seasons,
                 List<Genres> genres,
                 String type) {
    public Tv withImageUrl(String imageUrl, String backdropUrl) {
        return new Tv(adult(), originalName(), name(), imageUrl, backdropUrl, createdBy(), overview(), homepage(), originalLanguage(), id(), inProduction(), lastAirDate(), firstAirDate(), numberOfEpisodes(), numberOfSeasons(), releaseDate(), status(), seasons(), genres(), type());
    }
    public Tv withImageUrl(String imageUrl) {
        return new Tv(adult(), originalName(), name(), imageUrl, backdropPath(), createdBy(), overview(), homepage(), originalLanguage(), id(), inProduction(), lastAirDate(), firstAirDate(), numberOfEpisodes(), numberOfSeasons(), releaseDate(), status(), seasons(), genres(), type());
    }

    public Tv editSeasons(List<Season> seasons) {
        return new Tv(adult(), originalName(), name(), posterPath(), backdropPath(), createdBy(), overview(), homepage(), originalLanguage(), id(), inProduction(), lastAirDate(), firstAirDate(), numberOfEpisodes(), numberOfSeasons(), releaseDate(), status(), seasons, genres(), type());
    }
}

record Creators(String name) {
}
