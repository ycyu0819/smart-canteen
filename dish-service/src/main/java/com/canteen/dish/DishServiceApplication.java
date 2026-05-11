package com.canteen.dish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.canteen.dish", "com.canteen.common"})
public class DishServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DishServiceApplication.class, args);
    }
}
