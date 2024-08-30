package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "itemRequestRepos")
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("select ir " +
            "from ItemRequest as ir " +
            "join ir.requester as r " +
            "where r.id = :requester " +
            "group by ir.id " +
            "order by ir.created desc ")
    List<ItemRequest> findByOwnerId(@Param("requester") Long requester);

    @Query("select ir " +
            "from ItemRequest as ir " +
            "join ir.requester as r " +
            "where r.id != :requester " +
            "group by ir.id " +
            "order by ir.created desc ")
    List<ItemRequest> findByOwnerIdNotEquals(@Param("requester") Long requester);
}
