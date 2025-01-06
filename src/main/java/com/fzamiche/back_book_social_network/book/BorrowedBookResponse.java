package com.fzamiche.back_book_social_network.book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBookResponse {

    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private double rate; // moyenne des notes des feedbacks
    private boolean returned;
    private boolean returnApproved;
}
