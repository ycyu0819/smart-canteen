package com.canteen.dish.service;

import com.canteen.common.auth.UserContext;
import com.canteen.common.exception.BusinessException;
import com.canteen.common.response.ResultCode;
import com.canteen.dish.dto.DishRequest;
import com.canteen.dish.dto.DishResponse;
import com.canteen.dish.entity.Dish;
import com.canteen.dish.entity.SellingSlot;
import com.canteen.dish.repository.DishRepository;
import com.canteen.dish.repository.SellingSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.util.List;

@Service
public class DishService {
    private final DishRepository dishRepo;
    private final SellingSlotRepository slotRepo;

    public DishService(DishRepository dishRepo, SellingSlotRepository slotRepo) {
        this.dishRepo = dishRepo;
        this.slotRepo = slotRepo;
    }

    public DishResponse create(Long merchantId, DishRequest req) {
        Dish d = new Dish();
        d.setMerchantId(merchantId);
        d.setName(req.getName());
        d.setImageUrl(req.getImageUrl());
        d.setDescription(req.getDescription());
        d.setPrice(req.getPrice());
        d.setStock(req.getStock() != null ? req.getStock() : 0);
        d.setAlertThreshold(req.getAlertThreshold() != null ? req.getAlertThreshold() : 10);
        d.setCategoryId(req.getCategoryId());
        d.setStatus("OFF_SHELF");
        return DishResponse.from(dishRepo.save(d));
    }

    public DishResponse update(Long id, Long merchantId, DishRequest req) {
        Dish d = dishRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.DISH_NOT_FOUND));
        if (!d.getMerchantId().equals(merchantId)) {
            throw new BusinessException(400, "无权修改此菜品");
        }
        d.setName(req.getName());
        d.setImageUrl(req.getImageUrl());
        d.setDescription(req.getDescription());
        d.setPrice(req.getPrice());
        if (req.getStock() != null) d.setStock(req.getStock());
        if (req.getAlertThreshold() != null) d.setAlertThreshold(req.getAlertThreshold());
        if (req.getCategoryId() != null) d.setCategoryId(req.getCategoryId());
        return DishResponse.from(dishRepo.save(d));
    }

    @Transactional
    public void delete(Long id, Long merchantId) {
        Dish d = dishRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.DISH_NOT_FOUND));
        if (!d.getMerchantId().equals(merchantId)) {
            throw new BusinessException(400, "无权删除此菜品");
        }
        d.setIsDeleted(true);
        dishRepo.save(d);
    }

    public DishResponse getById(Long id) {
        return DishResponse.from(dishRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.DISH_NOT_FOUND)));
    }

    public List<DishResponse> getMerchantDishes(Long merchantId) {
        return dishRepo.findByMerchantIdAndIsDeletedFalse(merchantId)
                .stream().map(DishResponse::from).toList();
    }

    public List<DishResponse> getAvailable(Long merchantId) {
        List<Dish> dishes = dishRepo.findAvailableByMerchant(merchantId);
        LocalTime now = LocalTime.now();
        return dishes.stream()
                .filter(d -> isInSellingSlot(d.getId(), now))
                .map(DishResponse::from)
                .toList();
    }

    private boolean isInSellingSlot(Long dishId, LocalTime now) {
        List<SellingSlot> slots = slotRepo.findByDishId(dishId);
        if (slots.isEmpty()) return true; // 没有设置时段 = 全天可售
        return slots.stream().anyMatch(s ->
                s.getIsActive() && !now.isBefore(s.getStartTime()) && !now.isAfter(s.getEndTime()));
    }

    @Transactional
    public void toggleStatus(Long id, Long merchantId) {
        Dish d = dishRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.DISH_NOT_FOUND));
        if (!d.getMerchantId().equals(merchantId)) {
            throw new BusinessException(400, "无权操作此菜品");
        }
        d.setStatus("ON_SHELF".equals(d.getStatus()) ? "OFF_SHELF" : "ON_SHELF");
        dishRepo.save(d);
    }

    public List<DishResponse> getAll() {
        return dishRepo.findAllByIsDeletedFalse().stream()
                .map(DishResponse::from).toList();
    }

    public void forceOff(Long id) {
        Dish d = dishRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.DISH_NOT_FOUND));
        d.setStatus("OFF_SHELF");
        dishRepo.save(d);
    }
}
