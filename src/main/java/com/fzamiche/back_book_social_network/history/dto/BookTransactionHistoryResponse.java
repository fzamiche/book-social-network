package com.fzamiche.back_book_social_network.history.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookTransactionHistoryResponse {

    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private double rate; // moyenne des notes des feedbacks
    private boolean returned;
    private boolean returnApproved;
}
