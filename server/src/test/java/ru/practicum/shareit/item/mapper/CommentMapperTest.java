package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.comment.CommentInDto;
import ru.practicum.shareit.item.dto.comment.CommentOutDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {
    private final CommentInDto commentInDto = CommentInDto.builder()
            .text("Text")
            .build();
    private final CommentOutDto commentOutDto = CommentOutDto.builder()
            .id(1L)
            .text("Text")
            .authorName("Name")
            .created(LocalDateTime.now())
            .build();
    private final Comment comment = Comment.builder()
            .id(1L)
            .text("Text")
            .author(User.builder().name("Name").build())
            .created(LocalDateTime.now())
            .build();
    @InjectMocks
    private CommentMapper mapper;

    @Test
    void toComment() {
        assertEquals(comment.getText(), mapper.toComment(commentInDto).getText());
    }

    @Test
    void toDto() {
        assertEquals(commentOutDto.getId(), mapper.toDto(comment).getId());
        assertEquals(commentOutDto.getText(), mapper.toDto(comment).getText());
        assertEquals(commentOutDto.getAuthorName(), mapper.toDto(comment).getAuthorName());
    }
}
