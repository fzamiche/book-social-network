package com.fzamiche.back_book_social_network.book;

import org.springframework.data.jpa.domain.Specification;


/**
 * Est utilisée pour créer des spécifications de requêtes dynamiques pour l'entité `Book`
 * Une spécification est une condition de filtrage qui peut être utilisée
 * pour construire des requêtes de manière flexible.
 *
 * Cette classe permet de créer des critères de filtrage réutilisables pour les requêtes JPA, facilitant ainsi la construction
 * de requêtes complexes de manière modulaire et dynamique.
 */
public class BookSpecification {

    /*
     * Selectionne uniquement les livres dont l'identifiant du propriétaire est égal à ownerId.
     */
    public static Specification<Book> withOwnerId(Integer ownerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }
}
