package com.fzamiche.back_book_social_network.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "user")
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

    //private List<Role> roles;


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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
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

    private String getFullName() {
        return firstname + " " + lastname;
    }
}
