package hdm.stuttgart.geekslist.Database;

import hdm.stuttgart.geekslist.Entities.MediaType;
import hdm.stuttgart.geekslist.Entities.WatchlistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends PagingAndSortingRepository<WatchlistItem, Long> {
    Optional<WatchlistItem> findById(long id);
    WatchlistItem save(WatchlistItem item);
    void deleteById(long id);

    boolean existsByAccountIdAndMediaId(String accountId, String mediaId);

    Optional<WatchlistItem> findByAccountIdAndTypeAndMediaId(String accountId, MediaType type, String mediaId);

    List<WatchlistItem> findByTypeAndAccountId(MediaType type, String accountId);

    List<WatchlistItem> findAllByAccountId(String accountId);
    Page<WatchlistItem> findAllByAccountIdAndType(String accountId, MediaType type, Pageable page);
    Page<WatchlistItem> findAllByAccountIdAndTypeAndIsWatchedFalse(String accountId, MediaType type, Pageable page);
}
