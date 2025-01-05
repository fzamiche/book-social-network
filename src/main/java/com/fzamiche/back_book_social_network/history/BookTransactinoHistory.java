package com.fzamiche.back_book_social_network.history;

import com.fzamiche.back_book_social_network.book.Book;
import com.fzamiche.back_book_social_network.common.BaseEntity;
import com.fzamiche.back_book_social_network.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

//Lombok
@Getter
@Setter
@SuperBuilder //permet aux classes filles de récupérer les attributs de la classe mère, sans avoir à les redéclarer
@AllArgsConstructor
@NoArgsConstructor

@Entity
/*
    Cette classe représente l'historique des transactions de livre.
    Elle est liée à la classe User et à la classe Book.
    Dans le diagramme de classe, il y a une relation many to many entre User et Book
    c'est pourquoi il y a une classe intermédiaire BookTransactionHistory
    qui permet de tracker les transactions du livre.
    ref : diagramme de classe
 */
public class BookTransactinoHistory extends BaseEntity {

    //user relationship
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //book relationship
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private boolean returned; //si le livre a été retourné
    private boolean returnApproved; //si le retour a été approuvé
}
