package com.fzamiche.back_book_social_network.book;

import com.fzamiche.back_book_social_network.common.PageResponse;
import com.fzamiche.back_book_social_network.history.BookTransactinoHistory;
import com.fzamiche.back_book_social_network.history.BookTransactionHostoryRepository;
import com.fzamiche.back_book_social_network.history.BookTransactionHistoryResponse;
import com.fzamiche.back_book_social_network.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.fzamiche.back_book_social_network.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepositiry;
    private final BookTransactionHostoryRepository bookTransactionHostoryRepository;

    public Integer save(@Valid BookRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        return bookRepositiry.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {
        return bookRepositiry.findById(bookId)
                .map(bookMapper::toBookResponse) // extract the bookResponse from the book
                .orElseThrow(() -> new EntityNotFoundException("No Book found with ID :" + bookId));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser){
        User user = (User) connectedUser.getPrincipal();

        // permet de récupérer les livres par page et par taille de page, et les trier par date de création décroissante
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        // récupérer les livres paginés, non archivé, partageable, excepté ceux de l'utilisateur connecté
        Page<Book> books = bookRepositiry.findAllDisplayableBooks(pageable, user.getId());

        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepositiry.findAll(withOwnerId(user.getId()), pageable);
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookTransactionHistoryResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactinoHistory> allBorrowedBooks = bookTransactionHostoryRepository.findAllBorrowedBooks(pageable, user.getId());
        List<BookTransactionHistoryResponse> borrowedBookResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBookTransactionHistoryResponse)
                .toList();
        return new PageResponse<>(
                borrowedBookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BookTransactionHistoryResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactinoHistory> allReturnedBooks = bookTransactionHostoryRepository.findAllReturnedBooks(pageable, user.getId());
        List<BookTransactionHistoryResponse> returnedBookResponses = allReturnedBooks.stream()
                .map(bookMapper::toBookTransactionHistoryResponse)
                .toList();
        return new PageResponse<>(
                returnedBookResponses,
                allReturnedBooks.getNumber(),
                allReturnedBooks.getSize(),
                allReturnedBooks.getTotalElements(),
                allReturnedBooks.getTotalPages(),
                allReturnedBooks.isFirst(),
                allReturnedBooks.isLast()
        );
    }
}














