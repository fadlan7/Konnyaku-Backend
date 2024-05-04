package com.enigma.konyaku.specification;

import com.enigma.konyaku.dto.request.SearchRequest;
import com.enigma.konyaku.dto.request.SearchShopRequest;
import com.enigma.konyaku.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ShopSpecification {
    public static Specification<User> getSpecification(SearchShopRequest request) {
        return (r, q, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(request.getName() != null) {
                Predicate namePredicate = cb.like(cb.lower(r.get("name")), "%"+request.getName().toLowerCase()+"%");
                predicates.add(namePredicate);
            }

            if (request.getActivity() != null) {
                Predicate activityPredicate = cb.equal(r.get("activity"), request.getActivity());
                predicates.add(activityPredicate);
            }

            if (request.getAvailability() != null) {
                Predicate availabilityPredicate = cb.equal(r.get("availability"), request.getAvailability());
                predicates.add(availabilityPredicate);
            }

            return q.where(
                    cb.and(predicates.toArray(new Predicate[]{}))
            ).getRestriction();
        };
    }
}
