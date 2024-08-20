package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository(value = "itemRepos")
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findItemsByOwnerId(Long userId);

    @Query("SELECT item" +
            " FROM Item as item" +
            " WHERE item.available = true AND (item.name ilike %:searchText% OR item.description ilike %:searchText%)")
    List<Item> searchItemsByText(@Param("searchText") String text);
}
