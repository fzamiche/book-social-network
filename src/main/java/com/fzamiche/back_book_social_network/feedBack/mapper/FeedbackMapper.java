package com.fzamiche.back_book_social_network.feedBack.mapper;

import com.fzamiche.back_book_social_network.book.model.Book;
import com.fzamiche.back_book_social_network.feedBack.dto.FeedBackRequest;
import com.fzamiche.back_book_social_network.feedBack.dto.FeedBackResponse;
import com.fzamiche.back_book_social_network.feedBack.model.FeedBack;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {
    public FeedBack toFeedback(@Valid FeedBackRequest request) {
        return FeedBack.builder()
                .note(request.note())
                .comment(request.comment())
                .book(Book.builder()
                        .id(request.bookId())
                        .archived(false)
                        .shareable(false)
                        .build())
                .build();
    }

    public FeedBackResponse toFeedbackResponse(FeedBack feedback, Integer userId) {
        return FeedBackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), userId))
                .build();
    }
}
