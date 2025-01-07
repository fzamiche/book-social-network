package com.fzamiche.back_book_social_network.feedBack.controller;


import com.fzamiche.back_book_social_network.common.PageResponse;
import com.fzamiche.back_book_social_network.feedBack.dto.FeedBackRequest;
import com.fzamiche.back_book_social_network.feedBack.dto.FeedBackResponse;
import com.fzamiche.back_book_social_network.feedBack.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;


    @PostMapping
    public ResponseEntity<Integer> saveFeedback(
            @Valid @RequestBody FeedBackRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedbackService.saveFeedback(request, connectedUser));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedBackResponse>> findAllFeedbacksByBook(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedbackService.findAllFeedbacksByBook(page, size, bookId, connectedUser));
    }

}
