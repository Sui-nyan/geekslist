package hdm.stuttgart.geekslist.Models;

import com.google.gson.annotations.SerializedName;

public record Cast(String name,
                   String character,
                   @SerializedName("profile_path") String profilePath,
                   @SerializedName("known_for_department") String department) {
    public Cast withImageUrl(String imageUrl) {
        return new Cast(name(), character(), imageUrl, department());
    }
}
