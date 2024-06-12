package hdm.stuttgart.geekslist.Models;

import com.google.gson.annotations.SerializedName;

public record Results<TItems>(
        Integer page,
        @SerializedName("total_pages")
        Integer totalPages,
        TItems[] results,
        @SerializedName("total_results")
        Integer totalResults){}