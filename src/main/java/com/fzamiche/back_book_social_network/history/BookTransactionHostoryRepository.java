package com.fzamiche.back_book_social_network.history;

import com.fzamiche.back_book_social_network.history.model.BookTransactinoHistory;
import com.fzamiche.back_book_social_network.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookTransactionHostoryRepository extends JpaRepository<BookTransactinoHistory, Integer> {

    // récupérer les livres paginés, empruntés par l'utilisateur connecté
    @Query("""
            SELECT history
            FROM BookTransactinoHistory history
                WHERE history.user.id = :userId
            """)
    Page<BookTransactinoHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);

    // récupérer les livres paginés, retournés à l'utilisateur connecté.
    @Query("""
            SELECT history
            FROM BookTransactinoHistory history
                WHERE history.book.owner.id = :userId
            """)
    Page<BookTransactinoHistory> findAllReturnedBooks(Pageable pageable, Integer userId);

    @Query("""
           SELECT
                (COUNT(*) > 0) AS isBorrowed
           FROM BookTransactinoHistory history
               WHERE history.user.id =:userId
               AND history.book.id = :bookId
               AND history.returnApproved = false
           """)
    boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId);

    @Query("""
           SELECT history
           FROM BookTransactinoHistory history
               WHERE history.user.id =:userId
               AND history.book.id = :bookId
               AND history.returned = false
               AND history.returnApproved = false
           """)
    Optional<BookTransactinoHistory> findByBookIdAndUserId(Integer bookId, User userId);

    @Query("""
           SELECT history
           FROM BookTransactinoHistory history
               WHERE history.book.owner.id =:ownerId
               AND history.book.id = :bookId
               AND history.returned = true
               AND history.returnApproved = false
          """)
    Optional<BookTransactinoHistory> findByBookIdAndOwnerId(Integer bookId, User ownerId);
}
