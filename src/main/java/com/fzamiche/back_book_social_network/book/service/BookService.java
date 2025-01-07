package com.fzamiche.back_book_social_network.book.service;

import com.fzamiche.back_book_social_network.book.dto.BookRequest;
import com.fzamiche.back_book_social_network.book.dto.BookResponse;
import com.fzamiche.back_book_social_network.book.mapper.BookMapper;
import com.fzamiche.back_book_social_network.book.model.Book;
import com.fzamiche.back_book_social_network.book.repository.BookRepository;
import com.fzamiche.back_book_social_network.common.PageResponse;
import com.fzamiche.back_book_social_network.exception.OperationNotPermittedException;
import com.fzamiche.back_book_social_network.file.FileStorageService;
import com.fzamiche.back_book_social_network.history.model.BookTransactinoHistory;
import com.fzamiche.back_book_social_network.history.BookTransactionHostoryRepository;
import com.fzamiche.back_book_social_network.history.dto.BookTransactionHistoryResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.fzamiche.back_book_social_network.book.model.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHostoryRepository bookTransactionHostoryRepository;
    private final FileStorageService fileStorageService;

    public Integer save(@Valid BookRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse) // extract the bookResponse from the book
                .orElseThrow(() -> new EntityNotFoundException("No Book found with ID :" + bookId));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser){
        User user = (User) connectedUser.getPrincipal();

        // permet de récupérer les livres par page et par taille de page, et les trier par date de création décroissante
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        // récupérer les livres paginés, non archivé, partageable, excepté ceux de l'utilisateur connecté
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());

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
        Page<Book> books = bookRepository.findAll(withOwnerId(user.getId()), pageable);
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

    public Integer updateShareableStatusBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Le Book non trouvé avec l'ID : " + bookId));
        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("Vous ne pouvez pas mettre à jour le statut shareable du Book : " + bookId);
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    public Integer updateArchivedStatusBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Le Book non trouvé avec l'ID : " + bookId));
        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("Vous ne pouvez pas mettre à jour le statut archived du Book : " + bookId);
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Le Book non trouvé avec l'ID : " + bookId));

        // check si Book n'est pas archivé etpartageable
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("Vous ne pouvez pas emprunté le Book : " + bookId + " car archivé/non-partageable.");
        }
        User user = (User) connectedUser.getPrincipal();

        //l'utilisateur ne doit pas etre l'Owner du Book
        if(Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("Vous ne pouvez pas malheureusement emprunté votre Book : " + bookId );
        }

        // check si l'utilisateur n'as pas un emprunt en cours de ce Book
        final boolean isAlreadyBorrowed = bookTransactionHostoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if(isAlreadyBorrowed){
            throw new OperationNotPermittedException("Le Book : " + bookId + "que vous avez demandé est déjà en cours d'emprunt par vous" );
        }
        BookTransactinoHistory bookTransactinoHistory = BookTransactinoHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return bookTransactionHostoryRepository.save(bookTransactinoHistory).getId();
    }

    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Le Book non trouvé avec l'ID : " + bookId));

        // check si Book n'est pas archivé etpartageable
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("Vous ne pouvez pas retourné le Book : " + bookId + " car archivé/non-partageable.");
        }
        User user = (User) connectedUser.getPrincipal();

        //l'utilisateur ne doit pas etre l'Owner du Book
        if(Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("Vous ne pouvez pas malheureusement retourné votre Book : " + bookId );
        }

        // check si l'utilisateur a emprunté le Book
        BookTransactinoHistory bookTransactinoHistory = bookTransactionHostoryRepository.findByBookIdAndUserId(bookId, user)
                .orElseThrow(() -> new OperationNotPermittedException("Vous n'avez pas emprunté le Book avec l'ID : " + bookId));
        bookTransactinoHistory.setReturned(true);
        return bookTransactionHostoryRepository.save(bookTransactinoHistory).getId();
    }

    public Integer approuveReturnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Le Book non trouvé avec l'ID : " + bookId));

        // check si Book n'est pas archivé etpartageable
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("Vous ne pouvez pas approuvé le Book : " + bookId + " car archivé/non-partageable.");
        }
        User owner = (User) connectedUser.getPrincipal();
        // l'utilisateur doit être l'Owner du Book
        if(!Objects.equals(book.getOwner().getId(), owner.getId())){
            throw new OperationNotPermittedException("Vous ne pouvez pas approuver le retour du Book : " + bookId + " car vous n'êtes pas le propriétaire.");
        }

        // check si l'utilisateur a retourné le Book
        BookTransactinoHistory bookTransactinoHistory = bookTransactionHostoryRepository.findByBookIdAndOwnerId(bookId, owner)
                .orElseThrow(() -> new OperationNotPermittedException("Book avec l'ID : " + bookId + " n'a pas été encore retourné"));
        bookTransactinoHistory.setReturnApproved(true);
        return bookTransactionHostoryRepository.save(bookTransactinoHistory).getId();
    }

    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Integer bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Le Book non trouvé avec l'ID : " + bookId));
        User user = (User) connectedUser.getPrincipal();

        var bookCover = fileStorageService.saveFile(file, user);
        book.setBookCover(bookCover);
        bookRepository.save(book);
    }
}














