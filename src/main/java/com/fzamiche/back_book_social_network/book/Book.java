package com.fzamiche.back_book_social_network.book;

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
public class Book extends BaseEntity {

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;

}
