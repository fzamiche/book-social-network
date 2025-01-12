package com.fzamiche.back_book_social_network.feedBack.service;

import com.fzamiche.back_book_social_network.book.model.Book;
import com.fzamiche.back_book_social_network.book.repository.BookRepository;
import com.fzamiche.back_book_social_network.common.PageResponse;
import com.fzamiche.back_book_social_network.exception.OperationNotPermittedException;
import com.fzamiche.back_book_social_network.feedBack.dto.FeedBackRequest;
import com.fzamiche.back_book_social_network.feedBack.dto.FeedBackResponse;
import com.fzamiche.back_book_social_network.feedBack.repository.FeedbackRepository;
import com.fzamiche.back_book_social_network.feedBack.mapper.FeedbackMapper;
import com.fzamiche.back_book_social_network.feedBack.model.FeedBack;
import com.fzamiche.back_book_social_network.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;

    public Integer saveFeedback(@Valid FeedBackRequest request, Authentication connectedUser) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Le Book non trouvé avec l'ID : " + request.bookId()));

        // check si Book n'est pas archivé etpartageable
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("Vous ne pouvez pas noté le Book : " + request.bookId() + " car archivé/non-partageable.");
        }
        User user = (User) connectedUser.getPrincipal();

        //l'utilisateur ne doit pas etre l'Owner du Book
        if(Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("Vous ne pouvez pas malheureusement noté votre Book : " + request.bookId() );
        }

        FeedBack feedBack = feedbackMapper.toFeedback(request);
        return feedbackRepository.save(feedBack).getId();
    }

    public PageResponse<FeedBackResponse> findAllFeedbacksByBook(int page, int size, Integer bookId, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = (User) connectedUser.getPrincipal();
        Page<FeedBack> feedBacks = feedbackRepository.findAllFeedbacksByBookId(bookId, pageable);
        List<FeedBackResponse> feedBackResponses = feedBacks.stream()
                .map(feedback -> feedbackMapper.toFeedbackResponse(feedback, user.getId()))
                .toList();
        return new PageResponse<>(
                feedBackResponses,
                feedBacks.getNumber(),
                feedBacks.getSize(),
                feedBacks.getTotalElements(),
                feedBacks.getTotalPages(),
                feedBacks.isFirst(),
                feedBacks.isLast()
        );
    }
}
