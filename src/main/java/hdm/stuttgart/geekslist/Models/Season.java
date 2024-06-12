package hdm.stuttgart.geekslist.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record Season(@SerializedName("air_date") String AirDate,
                     @SerializedName("episode_count") int episodeCount,
                     int id,
                     String name,
                     String overview,
                     @SerializedName("poster_path") String posterPath,
                     @SerializedName("season_number") int seasonNumber,
                     List<Episode> episodes
) {
    public Season withImageUrl(String imageUrl) {
        return new Season(AirDate(), episodeCount(), id(), name(), overview(), imageUrl, seasonNumber(), episodes());
    }
}
