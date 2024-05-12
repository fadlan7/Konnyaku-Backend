package com.enigma.konyaku.specification;

import com.enigma.konyaku.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RandomProductSpecification {
    public static Specification<Product> getSpecification(String q) {
        return (root, query, cb) -> {

            if (!StringUtils.hasText(q)) return cb.conjunction();

//            List<Predicate> predicates = new ArrayList<>();
//            predicates.add(cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%"));
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.notEqual(root.get("status"), 2));

            predicates.add(cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%"));

            try {
                Long price = Long.valueOf(q);
                predicates.add(cb.equal(root.get("price"), price));
            } catch (NumberFormatException e) {
                String[] priceRange = q.split("-");
                if (priceRange.length == 2) {
                    Long minPrice = Long.valueOf(priceRange[0]);
                    Long maxPrice = Long.valueOf(priceRange[1]);
                    predicates.add(cb.between(root.get("price"), minPrice, maxPrice));
                }
            }


            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
