package com.canteen.dish.service;

import com.canteen.common.exception.BusinessException;
import com.canteen.common.response.ResultCode;
import com.canteen.dish.entity.Dish;
import com.canteen.dish.entity.StockLog;
import com.canteen.dish.repository.DishRepository;
import com.canteen.dish.repository.StockLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {
    private final DishRepository dishRepo;
    private final StockLogRepository stockLogRepo;

    public StockService(DishRepository dishRepo, StockLogRepository stockLogRepo) {
        this.dishRepo = dishRepo;
        this.stockLogRepo = stockLogRepo;
    }

    @Transactional
    public void deduct(Long dishId, int quantity, Long orderId) {
        Dish dish = dishRepo.findById(dishId)
                .orElseThrow(() -> new BusinessException(ResultCode.DISH_NOT_FOUND));
        if ("OFF_SHELF".equals(dish.getStatus())) {
            throw new BusinessException(ResultCode.DISH_OFF_SHELF);
        }
        if (dish.getStock() < quantity) {
            throw new BusinessException(ResultCode.STOCK_INSUFFICIENT);
        }
        int before = dish.getStock();
        dish.setStock(before - quantity);
        dishRepo.save(dish);

        StockLog log = new StockLog();
        log.setDishId(dishId);
        log.setChangeType("DEDUCT");
        log.setDelta(-quantity);
        log.setBeforeStock(before);
        log.setAfterStock(dish.getStock());
        log.setRelatedOrderId(orderId);
        stockLogRepo.save(log);
    }

    @Transactional
    public void restore(Long dishId, int quantity, Long orderId) {
        Dish dish = dishRepo.findById(dishId)
                .orElseThrow(() -> new BusinessException(ResultCode.DISH_NOT_FOUND));
        int before = dish.getStock();
        dish.setStock(before + quantity);
        dishRepo.save(dish);

        StockLog log = new StockLog();
        log.setDishId(dishId);
        log.setChangeType("RESTORE");
        log.setDelta(quantity);
        log.setBeforeStock(before);
        log.setAfterStock(dish.getStock());
        log.setRelatedOrderId(orderId);
        stockLogRepo.save(log);
    }

    @Transactional
    public void manualUpdate(Long dishId, int newStock) {
        Dish dish = dishRepo.findById(dishId)
                .orElseThrow(() -> new BusinessException(ResultCode.DISH_NOT_FOUND));
        int before = dish.getStock();
        dish.setStock(newStock);
        dishRepo.save(dish);

        StockLog log = new StockLog();
        log.setDishId(dishId);
        log.setChangeType("MANUAL");
        log.setDelta(newStock - before);
        log.setBeforeStock(before);
        log.setAfterStock(newStock);
        stockLogRepo.save(log);
    }
}
