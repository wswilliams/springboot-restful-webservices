package net.wssouza.springboot.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import net.wssouza.springboot.entity.Categoria;
import net.wssouza.springboot.repository.CategoriaRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoriaRepositoryImpl implements CategoriaRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Categoria> buscarPorNome(String nome, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Categoria> criteriaQuery = criteriaBuilder.createQuery(Categoria.class);
        Root<Categoria> root = criteriaQuery.from(Categoria.class);

        if (nome == null || nome.isEmpty()) {
            // Se o nome for null ou vazio, retorna todas as categorias
            criteriaQuery.select(root);
        } else {
            // Caso contrário, aplica a filtragem pelo nome
            Predicate nomePredicate = criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
            criteriaQuery.where(nomePredicate);
        }

        List<Categoria> categorias = entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long total = getTotalCount(nome);

        return new PageImpl<>(categorias, pageable, total);
    }

    private long getTotalCount(String nome) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Categoria> countRoot = countQuery.from(Categoria.class);

        if (nome == null || nome.isEmpty()) {
            countQuery.select(criteriaBuilder.count(countRoot)); // Conta todas as categorias
        } else {
            Predicate nomePredicate = criteriaBuilder.like(countRoot.get("nome"), "%" + nome + "%");
            countQuery.select(criteriaBuilder.count(countRoot)).where(nomePredicate);
        }

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}