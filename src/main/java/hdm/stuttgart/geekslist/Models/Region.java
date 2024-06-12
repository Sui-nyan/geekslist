package hdm.stuttgart.geekslist.Models;

import com.google.gson.annotations.SerializedName;

public record Region(
        @SerializedName("iso_3166_1")
        String isoCode,
        @SerializedName("english_name")
        String englishName,
        @SerializedName("native_name")
        String nativeName
        ) {}