package com.fzamiche.back_book_social_network.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookTransactionHostoryRepository extends JpaRepository<BookTransactinoHistory, Integer> {

    // récupérer les livres paginés, non archivé, partageable, excepté ceux de l'utilisateur connecté
    @Query("""
            SELECT history
            FROM BookTransactinoHistory history
            WHERE history.user.id = :userId
            """)
    Page<BookTransactinoHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);
}
