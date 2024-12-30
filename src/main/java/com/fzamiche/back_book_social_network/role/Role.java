package com.fzamiche.back_book_social_network.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fzamiche.back_book_social_network.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)  //Capturer les événements de cycle de vie de l'entité JPA et les déléguer à des auditeurs
public class Role {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore     // pour éviter la récursivité lors de la sérialisation en JSON de l'entité User qui contient une liste de rôles qui contient une liste d'utilisateurs qui contient une liste de rôles, etc.
    private List<User> users;


    @CreatedDate
    // pour que Spring Data JPA initialise la propriété avec la date et l'heure actuelles lors de la création de l'entité
    @Column(nullable = false, updatable = false)    // pour que la colonne ne puisse pas être nulle et ne puisse pas être mise à jour
    private LocalDateTime createdDate;
    @CreatedDate
    @Column(insertable = false) // pour que la colonne ne puisse pas être insérée
    private LocalDateTime lastModifiedDate;
}
