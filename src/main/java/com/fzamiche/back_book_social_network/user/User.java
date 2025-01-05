package com.fzamiche.back_book_social_network.user;

import com.fzamiche.back_book_social_network.book.Book;
import com.fzamiche.back_book_social_network.history.BookTransactinoHistory;
import com.fzamiche.back_book_social_network.role.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)  //Capturer les événements de cycle de vie de l'entité JPA et les déléguer à des auditeurs
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    @Column(unique = true)
    private String email; // unique identifier de l'utilisateur dans le système
    private String password;
    private boolean accountLocked;
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER) // pour charger les rôles de l'utilisateur en même temps que l'utilisateur
    private List<Role> roles;

    @OneToMany(mappedBy = "owner") // un utilisateur peut avoir plusieurs livres, un livre appartient à un seul utilisateur
    private List<Book> books;

    @OneToMany(mappedBy = "user") // un livre peut avoir plusieurs historiques de transaction, un historique de transaction appartient à un seul livre
    private List<BookTransactinoHistory> histories;


    @CreatedDate    // pour que Spring Data JPA initialise la propriété avec la date et l'heure actuelles lors de la création de l'entité
    @Column(nullable = false, updatable = false)    // pour que la colonne ne puisse pas être nulle et ne puisse pas être mise à jour
    private LocalDateTime createdDate;
    @CreatedDate
    @Column(insertable = false) // pour que la colonne ne puisse pas être insérée
    private LocalDateTime lastModifiedDate;


    @Override
    public String getName() {
        return email;
    }

    /**
     * Retourne les rôles de l'utilisateur sous forme d'autorisations.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { // le compte n'est pas expiré
        return true ;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }
}
