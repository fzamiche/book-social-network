package com.fzamiche.back_book_social_network.book;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public Book toBook(@Valid BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .isbn(request.isbn())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }
}
