package com.fzamiche.back_book_social_network.book;

import com.fzamiche.back_book_social_network.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepositiry;

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
}
