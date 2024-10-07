package net.wssouza.springboot.repository.impl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import net.wssouza.springboot.entity.Produto;
import net.wssouza.springboot.repository.ProdutoRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class ProdutoRepositoryImpl  implements ProdutoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Produto> listarProdutos(Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);
        criteriaQuery.select(root);
        List<Produto> produtos = entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long total = getTotalCount();

        return new PageImpl<>(produtos, pageable, total);
    }

    @Override
    public Page<Produto> buscarPorNomeOrDescricao(String nome, String descricao, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        if ((nome == null || nome.isEmpty()) && (descricao == null || descricao.isEmpty())) {
            criteriaQuery.select(root);
        } else {

            List<Predicate> predicates = new ArrayList<>();

            if (nome != null && !nome.isEmpty()) {
                Predicate nomePredicate = criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
                predicates.add(nomePredicate);
            }

            if (descricao != null && !descricao.isEmpty()) {
                Predicate descricaoPredicate = criteriaBuilder.like(root.get("descricao"), "%" + descricao + "%");
                predicates.add(descricaoPredicate);
            }

            criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
        }

        List<Produto> produtos = entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long total = getTotalCount(nome, descricao);

        return new PageImpl<>(produtos, pageable, total);
    }
    private long getTotalCount(String nome, String descricao) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Produto> countRoot = countQuery.from(Produto.class);

        List<Predicate> predicates = new ArrayList<>();

        if (nome != null && !nome.isEmpty()) {
            Predicate nomePredicate = criteriaBuilder.like(countRoot.get("nome"), "%" + nome + "%");
            predicates.add(nomePredicate);
        }

        if (descricao != null && !descricao.isEmpty()) {
            Predicate descricaoPredicate = criteriaBuilder.like(countRoot.get("descricao"), "%" + descricao + "%");
            predicates.add(descricaoPredicate);
        }

        countQuery.select(criteriaBuilder.count(countRoot)).where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private long getTotalCount() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Produto> countRoot = countQuery.from(Produto.class);

        List<Predicate> predicates = new ArrayList<>();

        countQuery.select(criteriaBuilder.count(countRoot)).where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
