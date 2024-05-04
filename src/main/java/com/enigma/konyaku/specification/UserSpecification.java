package com.enigma.konyaku.specification;

import com.enigma.konyaku.dto.request.SearchRequest;
import com.enigma.konyaku.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<User> getSpecification(SearchRequest request) {
        return (r, q, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(request.getName() != null) {
                Predicate namePredicate = cb.like(cb.lower(r.get("name")), "%"+request.getName().toLowerCase()+"%");
                predicates.add(namePredicate);
            }

            return q.where(
                    cb.and(predicates.toArray(new Predicate[]{}))
            ).getRestriction();
        };
    }
}
