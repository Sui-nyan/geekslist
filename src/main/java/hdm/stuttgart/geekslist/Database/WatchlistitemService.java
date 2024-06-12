package hdm.stuttgart.geekslist.Database;

import hdm.stuttgart.geekslist.Entities.MediaType;
import hdm.stuttgart.geekslist.Entities.WatchlistItem;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class WatchlistitemService {
    private final WatchlistRepository watchlistRepository;

    @Autowired
    public WatchlistitemService(WatchlistRepository repository) {
        watchlistRepository = repository;
    }

    public List<WatchlistItem> getAllWatchlistItems() {
        return StreamSupport.stream(watchlistRepository.findAll(Sort.unsorted()).spliterator(), false).collect(Collectors.toList());
    }

    public WatchlistItem getWatchlistItemById(long id) {
        return watchlistRepository.findById(id).orElse(null);
    }

    public WatchlistItem saveOrUpdateWatchlistItem(WatchlistItem watchlistItem) {
        return watchlistRepository.save(watchlistItem);
    }

    public void deleteWatchlistItem(long id) {
        watchlistRepository.deleteById(id);
    }

    public List<WatchlistItem> findByType(MediaType type, String accountId) {
        return watchlistRepository.findByTypeAndAccountId(type, accountId);
    }

    public boolean existsByAccountIdAndMediaId(String accountId, String mediaId) {
        return watchlistRepository.existsByAccountIdAndMediaId(accountId, mediaId);
    }

    public List<WatchlistItem> findAllByAccountId(String accountId) {
        return watchlistRepository.findAllByAccountId(accountId);
    }
}
