package com.fzamiche.back_book_social_network.book.mapper;

import com.fzamiche.back_book_social_network.book.model.Book;
import com.fzamiche.back_book_social_network.book.dto.BookRequest;
import com.fzamiche.back_book_social_network.book.dto.BookResponse;
import com.fzamiche.back_book_social_network.file.FileUtils;
import com.fzamiche.back_book_social_network.history.model.BookTransactinoHistory;
import com.fzamiche.back_book_social_network.history.dto.BookTransactionHistoryResponse;
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
                .bookCover(FileUtils.readFileFromLocation(book.getBookCover()))
                .rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .build();
    }

    public BookTransactionHistoryResponse toBookTransactionHistoryResponse(BookTransactinoHistory bookTransactinoHistory) {
        return BookTransactionHistoryResponse.builder()
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
