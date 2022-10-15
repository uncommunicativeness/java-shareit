package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long id, Pageable pageable);

    @Query("select i from Item i " +
            "where lower(i.name) like lower(concat('%', :text,'%')) " +
            "or lower(i.description) like lower(concat('%', :text,'%')) " +
            "and i.available = true")
    List<Item> search(@Param("text") String text, Pageable pageable);
}