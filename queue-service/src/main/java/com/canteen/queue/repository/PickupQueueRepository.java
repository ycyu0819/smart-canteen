package com.canteen.queue.repository;

import com.canteen.queue.entity.PickupQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PickupQueueRepository extends JpaRepository<PickupQueue, Long> {
    Optional<PickupQueue> findByOrderId(Long orderId);
}
