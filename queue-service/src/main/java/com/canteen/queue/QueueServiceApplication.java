package com.canteen.queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.canteen.queue", "com.canteen.common"})
@EnableFeignClients
public class QueueServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(QueueServiceApplication.class, args);
    }
}
