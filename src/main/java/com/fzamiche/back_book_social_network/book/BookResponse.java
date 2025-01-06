package com.fzamiche.back_book_social_network.book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {

    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String owner;
    private byte[] bookCover;
    private double rate; // moyenne des notes des feedbacks
    private boolean archived;
    private boolean shareable;
}
