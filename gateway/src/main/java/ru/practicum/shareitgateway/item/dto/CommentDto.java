package ru.practicum.shareitgateway.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private  Long id;

    @NotBlank
    private String text;

    private String authorName;

    private Long itemId;

    private LocalDateTime created;

}
