package hdm.stuttgart.geekslist.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record TvResults(
        int page,
        List<Tv> results,
        @SerializedName("total_results") int totalResults) {
}
