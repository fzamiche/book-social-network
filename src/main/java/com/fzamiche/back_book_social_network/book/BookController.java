package com.fzamiche.back_book_social_network.book;

import com.fzamiche.back_book_social_network.common.PageResponse;
import com.fzamiche.back_book_social_network.history.BookTransactionHistoryResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Integer> saveBook(
            @Valid @RequestBody BookRequest request,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.save(request, connectedUser));
    }

    @GetMapping("{book-id}")
    public ResponseEntity<BookResponse> findBookById(
            @PathVariable("book-id") Integer bookId) {

        return ResponseEntity.ok(bookService.findById(bookId));
    }

    /**
     * Récupérer la liste des livres paginée, non archivé, partageable, excepté ceux de l'utilisateur connecté
     *
     * @param page          : la page à récupérer (0 par défaut)
     * @param size          : la taille de la page (10 par défaut)
     * @param connectedUser : l'utilisateur connecté (pour ne pas récupérer ses livres)
     *                      <p>
     *                      En utilisant la pagination, on peut récupérer les livres par page et par taille de page
     *                      Cela permet de ne pas surcharger la mémoire de l'application
     *                      Exemple : si on a 1000 livres, on peut les récupérer par 10, 20 ... livres par page
     * @return la liste des livres paginée
     */
    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBooks(page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBooksByOwner(page, size, connectedUser));
    }

    // récupérer les livres paginés, empruntés par l'utilisateur connecté
    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BookTransactionHistoryResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page, size, connectedUser));
    }

    // récupérer les livres paginés, retournés à l'utilisateur connecté.
    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BookTransactionHistoryResponse>> findAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page, size, connectedUser));
    }

    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<Integer> updateShareableStatusBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.updateShareableStatusBook(bookId, connectedUser));
    }

    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Integer> updateArchivedStatusBook(
           @PathVariable("book-id") Integer bookId,
           Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.updateArchivedStatusBook(bookId, connectedUser));
    }

}
