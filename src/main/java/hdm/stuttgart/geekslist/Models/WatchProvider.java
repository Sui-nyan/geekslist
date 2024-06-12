package hdm.stuttgart.geekslist.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public record WatchProvider(int id,
                            Map<String, ProviderCountry> results) {
}

