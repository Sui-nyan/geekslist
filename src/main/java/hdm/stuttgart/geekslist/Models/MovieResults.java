package hdm.stuttgart.geekslist.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record MovieResults(
        int page,
        List<Movie> results,
        @SerializedName("total_results") int totalResults) {

}

