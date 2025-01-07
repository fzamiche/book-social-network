package com.fzamiche.back_book_social_network.book.repository;

import com.fzamiche.back_book_social_network.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    // récupérer les livres paginés, non archivé, partageable, excepté ceux de l'utilisateur connecté
    @Query("""
            SELECT book
            FROM Book book
            WHERE book.archived = false
            AND book.shareable = true
            AND book.owner.id != :userId
            """)
    Page<Book> findAllDisplayableBooks(Pageable pageable, Integer userId);

    // récupérer les livres paginés, non archivé, partageable, excepté ceux de l'utilisateur connecté
    @Query("""
            SELECT book
            FROM Book book
            WHERE book.owner.id = :userId
            """)
    Page<Book> findAllDisplayableBooksByOwner(Pageable pageable, Integer userId);
}
