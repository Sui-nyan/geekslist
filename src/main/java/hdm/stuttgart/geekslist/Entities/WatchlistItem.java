package hdm.stuttgart.geekslist.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "watchlist", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"accountId", "mediaId"})
})
public class WatchlistItem {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    @JsonProperty("account_id")
    private String accountId;
    @Column(nullable = false)
    @JsonProperty("media_id")
    private String mediaId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType type;
    @Column(nullable = false)
    @JsonProperty("is_watched")
    private boolean isWatched;
    @JsonProperty("added_at")
    private Date addedAt;

    public Long getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public MediaType getType() {
        return type;
    }

    public boolean getIsWatched() {
        return isWatched;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public WatchlistItem() {

    }

    public WatchlistItem(long id, String accountId, String mediaId, MediaType type, boolean isWatched, Date addedAt) {
        this.id = id;
        this.accountId = accountId;
        this.mediaId = mediaId;
        this.type = type;
        this.isWatched = isWatched;
        this.addedAt = addedAt;
    }
}
