package com.canteen.dish.repository;

import com.canteen.dish.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByMerchantId(Long merchantId);
    int countByMerchantIdAndName(Long merchantId, String name);
}
