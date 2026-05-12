package com.canteen.queue.repository;

import com.canteen.queue.entity.CallingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallingLogRepository extends JpaRepository<CallingLog, Long> {
}
