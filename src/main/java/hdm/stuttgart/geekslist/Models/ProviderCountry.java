package hdm.stuttgart.geekslist.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record ProviderCountry(String link,
                              @SerializedName("flatrate") List<Provider> flatRate,
                              List<Provider> rent,
                              List<Provider> buy,
                              List<Provider> free) {
}
