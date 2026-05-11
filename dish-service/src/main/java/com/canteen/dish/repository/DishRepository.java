package com.canteen.dish.repository;

import com.canteen.dish.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByMerchantIdAndIsDeletedFalse(Long merchantId);

    @Query("SELECT d FROM Dish d WHERE d.status = 'ON_SHELF' AND d.isDeleted = false "
         + "AND d.stock > 0 AND d.merchantId = :merchantId "
         + "ORDER BY d.createdAt DESC")
    List<Dish> findAvailableByMerchant(Long merchantId);

    List<Dish> findAllByIsDeletedFalse();
}
