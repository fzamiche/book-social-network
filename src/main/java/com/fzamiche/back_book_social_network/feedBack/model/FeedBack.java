package com.fzamiche.back_book_social_network.feedBack.model;

import com.fzamiche.back_book_social_network.book.model.Book;
import com.fzamiche.back_book_social_network.common.BaseEntity;
import jakarta.persistence.*;
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
public class FeedBack extends BaseEntity {

    private Double note;
    private String comment;

    @ManyToOne // plusieurs feedbacks peuvent appartenir à un seul livre
    @JoinColumn(name = "book_id") // la clé étrangère dans la table FeedBack qui fait référence à la clé primaire de la table Book
    private Book book;
}
