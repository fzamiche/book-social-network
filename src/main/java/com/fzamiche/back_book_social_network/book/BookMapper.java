package com.fzamiche.back_book_social_network.book;

import com.fzamiche.back_book_social_network.history.BookTransactinoHistory;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public Book toBook(@Valid BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .isbn(request.isbn())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .owner(book.getOwner().getFullName())
                // todo - implémenter la récupération de l'image du livre
                //.bookCover(book.getBookCover())
                .rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactinoHistory bookTransactinoHistory) {
        return BorrowedBookResponse.builder()
                .id(bookTransactinoHistory.getId())
                .title(bookTransactinoHistory.getBook().getTitle())
                .authorName(bookTransactinoHistory.getBook().getAuthorName())
                .isbn(bookTransactinoHistory.getBook().getIsbn())
                .rate(bookTransactinoHistory.getBook().getRate())
                .returned(bookTransactinoHistory.isReturned())
                .returned(bookTransactinoHistory.isReturnApproved())
                .build();
    }
}
