package hdm.stuttgart.geekslist.Models;

import com.google.gson.annotations.SerializedName;

public record Provider(@SerializedName("provider_id") int providerId,
                       @SerializedName("provider_name") String providerName,
                       @SerializedName("logo_path") String logoPath) {
    public Provider withImageUrl(String imageUrl) {
        return new Provider(providerId(), providerName(), imageUrl);
    }
}
