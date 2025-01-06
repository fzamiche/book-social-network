package com.fzamiche.back_book_social_network.book;

import com.fzamiche.back_book_social_network.common.BaseEntity;
import com.fzamiche.back_book_social_network.feedBack.FeedBack;
import com.fzamiche.back_book_social_network.history.BookTransactinoHistory;
import com.fzamiche.back_book_social_network.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

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

    @ManyToOne // plusieurs livres peuvent appartenir à un seul utilisateur
    @JoinColumn(name = "owner_id") // la clé étrangère dans la table Book qui fait référence à la clé primaire de la table User
    private User owner;

    @OneToMany(mappedBy = "book") // un livre peut avoir plusieurs feedbacks, un feedback appartient à un seul livre
    private List<FeedBack> feedBacks;

    @Transient
    public double getRate() {
        if (feedBacks == null || feedBacks.isEmpty()) {
            return 0;
        } else {
            var rate =  feedBacks.stream()
                    .mapToDouble(FeedBack::getNote)
                    .average()
                    .orElse(0);
            var roundedRate = Math.round(rate * 10.0) / 10.0; // 3.34 -> 3.0 | 3.56 -> 4.0
            return roundedRate;
        }
    }


    @OneToMany(mappedBy = "book") // un livre peut avoir plusieurs historiques de transaction, un historique de transaction appartient à un seul livre
    private List<BookTransactinoHistory> histories;

}
