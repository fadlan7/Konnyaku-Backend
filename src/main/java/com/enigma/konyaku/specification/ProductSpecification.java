package com.enigma.konyaku.specification;

import com.enigma.konyaku.entity.Product;
import com.enigma.konyaku.entity.Shop;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> getSpecification(String q, String shopId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (q != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%"));
            }
            Join<Product, Shop> productJoin = root.join("shop", JoinType.INNER);
            predicates.add(cb.equal(productJoin.get("id"), shopId));
            // predicates.add(cb.equal(root.get("shop").get("id"), shopId));

            return cb.or(predicates.toArray(new Predicate[]{}));
        };
    }
}
