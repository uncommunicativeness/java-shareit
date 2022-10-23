package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends PagingAndSortingRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequestorId(Long id, Sort sort);

    @Query("select ir from ItemRequest  ir where ir.requestor.id <> :requestorId")
    List<ItemRequest> findOtherUserItems(@Param("requestorId") Long requestorId, Pageable pageable);
}