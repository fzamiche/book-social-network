package com.fzamiche.back_book_social_network.common;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

//Lombok
@Getter
@Setter
@SuperBuilder //permet aux classes filles de récupérer les attributs de la classe mère, sans avoir à les redéclarer
@AllArgsConstructor
@NoArgsConstructor

//JPA
@MappedSuperclass //permet de définir une classe qui ne sera pas une table dans la base de données, mais dont les attributs seront repris par les classes filles

//Auditing
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false) // on ne veut pas que la date de modification soit insérée lors de la création de l'entité
    private LocalDateTime lastModifiedDate;
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Integer createdBy;
    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;
}
