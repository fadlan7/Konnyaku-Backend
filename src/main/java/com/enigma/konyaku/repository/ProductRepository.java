package com.enigma.konyaku.repository;

import com.enigma.konyaku.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query(nativeQuery = true,
    value = """
            SELECT id, name, description, weight FROM m_product
            WHERE id IN
            (SELECT p.id, SUM(pd.price) FROM m_product AS p
            INNER JOIN m_product_detail AS pd ON p.id = pd.product_id
            GROUP BY (p.id)
            HAVING SUM(pd.price) > :minPrice AND SUM(pd.price) < :maxPrice)
            AND name LIKE LOWER(:name);
            """
    )
    public Optional<List<Product>> findAllFiltered(@Param("minPrice") Integer minPrice,
                                                   @Param("maxPrice") Integer maxPrice,
                                                   @Param("name") String name
                                                   );
}
