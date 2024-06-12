package hdm.stuttgart.geekslist.Models;

import com.google.gson.annotations.SerializedName;

public record Episode(
        @SerializedName("air_date")
        String airDate,
        @SerializedName("episode_number")
        int episodeNumber,
        String name,
        String overview,
        @SerializedName("season_number")
        int seasonNumber,
        @SerializedName("still_path")
        String stillPath,
        Integer runtime,
        int id
) {
        public Episode withImageUrl(String imageUrl) {
                return new Episode(airDate(), episodeNumber(), name(), overview(), seasonNumber(), imageUrl, runtime(), id());
        }
}
