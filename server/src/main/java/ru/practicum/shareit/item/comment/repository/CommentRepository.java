package ru.practicum.shareit.item.comment.repository;

import ru.practicum.shareit.item.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "commentRepos")
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByItemId(Long itemId);
}
