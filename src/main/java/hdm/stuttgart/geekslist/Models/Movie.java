package hdm.stuttgart.geekslist.Models;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;

import java.util.List;

@Builder
public record Movie(
        Boolean adult,
        @SerializedName("original_title") String originalTitle,
        String title,
        @SerializedName("poster_path") String posterPath,
        @SerializedName("backdrop_path") String backdropPath,
        String overview,
        String homepage,
        @SerializedName("original_language")
        String originalLanguage,
        int id,
        @SerializedName("release_date") String releaseDate,
        int runtime,
        String status,
        int budget,
        long revenue,
        List<Genres> genres) {

    public Movie withImageUrl(String imageUrl, String backdropUrl) {
        return Movie.builder()
                .adult(adult())
                .originalTitle(originalTitle())
                .title(title())
                .posterPath(imageUrl)
                .backdropPath(backdropUrl)
                .overview(overview())
                .homepage(homepage())
                .originalLanguage(originalLanguage())
                .id(id())
                .releaseDate(releaseDate())
                .runtime(runtime())
                .status(status())
                .budget(budget())
                .revenue(revenue())
                .genres(genres())
                .build();
    }

    public Movie withImageUrl(String imageUrl) {
        return Movie.builder()
                .adult(adult())
                .originalTitle(originalTitle())
                .title(title())
                .posterPath(imageUrl)
                .backdropPath(backdropPath())
                .overview(overview())
                .homepage(homepage())
                .originalLanguage(originalLanguage())
                .id(id())
                .releaseDate(releaseDate())
                .runtime(runtime())
                .status(status())
                .budget(budget())
                .revenue(revenue())
                .genres(genres())
                .build();
    }
}
