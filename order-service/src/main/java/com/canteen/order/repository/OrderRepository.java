package com.canteen.order.repository;

import com.canteen.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNo(String orderNo);
    Page<Order> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<Order> findByMerchantIdOrderByCreatedAtDesc(Long merchantId, Pageable pageable);
    List<Order> findByStatusAndCreatedAtBefore(String status, java.time.LocalDateTime before);
}
