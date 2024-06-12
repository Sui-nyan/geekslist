package hdm.stuttgart.geekslist.Controller;

import hdm.stuttgart.geekslist.Database.WatchlistRepository;
import hdm.stuttgart.geekslist.Database.WatchlistitemService;
import hdm.stuttgart.geekslist.Entities.MediaType;
import hdm.stuttgart.geekslist.Entities.WatchlistItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {
    public enum WatchlistOrder {
        ByDate, ByDateReverse, ByWatched, ByWatchedReverse
    }

    private final WatchlistitemService service;
    private final WatchlistRepository watchlistRepository;

    @Autowired
    public WatchlistController(WatchlistitemService service, WatchlistRepository repository) {
        this.service = service;
        this.watchlistRepository = repository;
    }

    @PutMapping
    public long addWatchlistItem(@RequestBody WatchlistItem watchlistItem) {
        boolean exists = service.existsByAccountIdAndMediaId(watchlistItem.getAccountId(), watchlistItem.getMediaId());
        if(exists) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Item already exists");
        }
        return service.saveOrUpdateWatchlistItem(watchlistItem).getId();
    }
    @PostMapping
    public void updateWatchlistItem(@RequestBody WatchlistItem watchlistItem) {
        service.saveOrUpdateWatchlistItem(watchlistItem);
        // code 200 OK will suffice as response
    }

    @GetMapping("/all")
    public List<WatchlistItem> getWatchlist() {
        return service.getAllWatchlistItems();
    }

    @GetMapping("/item")
    public WatchlistItem isWatchlisted(@RequestParam(name = "account_id") String accountId, @RequestParam(name = "media_id") String mediaId, @RequestParam(name = "type") String type) {
       try {
           MediaType mediaType = MediaType.valueOf(type.toUpperCase());
           return watchlistRepository.findByAccountIdAndTypeAndMediaId(accountId, mediaType, mediaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
       } catch (IllegalArgumentException ex) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Media type " + type + " is invalid");
       }
    }

    @GetMapping("/item/{id}")
    public WatchlistItem getWatchlistItemById(@PathVariable long id) {
        return service.getWatchlistItemById(id);
    }
    @DeleteMapping("/item/{id}")
    public void deleteWatchlistItem(@PathVariable long id) {
        service.deleteWatchlistItem(id);
    }


    @GetMapping(value = "account/{accountId}")
    public List<WatchlistItem> getWatchlistItemsByAccountId(@PathVariable String accountId) {
        return service.findAllByAccountId(accountId);
    }
    @GetMapping("/account/{accountId}/{type}")
    public List<WatchlistItem> getWatchlistitemsByAccountAndType(@PathVariable String accountId, @PathVariable String type) {
        if(type.equalsIgnoreCase("tv")){
            return service.findByType(MediaType.TV, accountId);
        } else if (type.equalsIgnoreCase("movie")) {
            return service.findByType(MediaType.MOVIE, accountId);
        } else {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid Type");
        }
    }
    @GetMapping(value = "account/{accountId}/{type}", params = {"page"})
    public Page<WatchlistItem> getWatchlistItemsByAccountId(
            @PathVariable String accountId,
            @PathVariable MediaType type,
            @RequestParam int page,
            @RequestParam(name="only_unwatched", defaultValue = "false") boolean onlyUnwatched,
            @RequestParam(name="page_length", defaultValue = "10") int itemsPerPage,
            @RequestParam(name="order", defaultValue = "ByWatched") WatchlistOrder watchlistOrder) {
        try {
            Pageable pageRequest;
            switch (watchlistOrder) {
                case ByDate -> pageRequest = PageRequest.of(page, itemsPerPage, Sort.by("addedAt").and(Sort.by("id")));
                case ByDateReverse -> pageRequest = PageRequest.of(page, itemsPerPage, Sort.by(Sort.Direction.DESC, "addedAt").and(Sort.by("id")));
                case ByWatched -> pageRequest = PageRequest.of(page, itemsPerPage, Sort.by("isWatched").and(Sort.by("addedAt")).and(Sort.by("id")));
                case ByWatchedReverse -> pageRequest = PageRequest.of(page, itemsPerPage, Sort.by(Sort.Direction.DESC, "isWatched").and(Sort.by("addedAt")).and(Sort.by("id")));
                default -> throw new IllegalStateException("Unexpected value: " + watchlistOrder);
            }

            Page<WatchlistItem> resultPage;
            if (onlyUnwatched) {
                resultPage = watchlistRepository.findAllByAccountIdAndTypeAndIsWatchedFalse(accountId, type, pageRequest);
            } else {
                resultPage = watchlistRepository.findAllByAccountIdAndType(accountId, type, pageRequest);
            }

            if (!resultPage.hasContent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page is empty");
            return resultPage;
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Sort method not yet supported by this endpoint");
        }
    }
}
