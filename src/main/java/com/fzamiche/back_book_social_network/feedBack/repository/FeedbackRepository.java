package com.fzamiche.back_book_social_network.feedBack.repository;


import com.fzamiche.back_book_social_network.feedBack.model.FeedBack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepository extends JpaRepository<FeedBack, Integer> {

    @Query("""
            SELECT feedback
            FROM FeedBack feedback
            WHERE feedback.book = :bookId                        
            """)
    Page<FeedBack> findAllFeedbacksByBookId(Integer bookId, Pageable pageable);
}
