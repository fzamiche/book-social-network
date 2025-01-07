package com.fzamiche.back_book_social_network.feedBack.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedBackResponse {

    private Double note;
    private String comment;
    private boolean ownFeedback;

}
