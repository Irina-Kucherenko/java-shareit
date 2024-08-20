package ru.practicum.shareit.item.comment.mapper;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;

public class CommentMapper {

    public static CommentDto transformToDto(Comment comment) {
       CommentDto commentDto = new CommentDto();
       commentDto.setId(comment.getId());
       commentDto.setAuthorName(comment.getAuthor().getName());
       commentDto.setText(comment.getText());
       commentDto.setCreated(comment.getCreated());
       return commentDto;
    }
}
