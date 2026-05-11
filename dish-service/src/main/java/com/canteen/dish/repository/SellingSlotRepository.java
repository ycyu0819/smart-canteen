package com.canteen.dish.repository;

import com.canteen.dish.entity.SellingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SellingSlotRepository extends JpaRepository<SellingSlot, Long> {
    List<SellingSlot> findByDishId(Long dishId);
    void deleteByDishId(Long dishId);
}
