package com.canteen.dish.service;

import com.canteen.common.exception.BusinessException;
import com.canteen.dish.dto.CategoryRequest;
import com.canteen.dish.entity.Category;
import com.canteen.dish.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepo;

    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public Category create(Long merchantId, CategoryRequest req) {
        Category c = new Category();
        c.setMerchantId(merchantId);
        c.setName(req.getName());
        c.setSortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0);
        return categoryRepo.save(c);
    }

    public List<Category> getByMerchant(Long merchantId) {
        return categoryRepo.findByMerchantId(merchantId);
    }

    @Transactional
    public void update(Long id, Long merchantId, CategoryRequest req) {
        Category c = categoryRepo.findById(id)
                .orElseThrow(() -> new BusinessException(400, "分类不存在"));
        if (!c.getMerchantId().equals(merchantId)) {
            throw new BusinessException(400, "无权操作此分类");
        }
        c.setName(req.getName());
        if (req.getSortOrder() != null) c.setSortOrder(req.getSortOrder());
        categoryRepo.save(c);
    }

    @Transactional
    public void delete(Long id, Long merchantId) {
        Category c = categoryRepo.findById(id)
                .orElseThrow(() -> new BusinessException(400, "分类不存在"));
        if (!c.getMerchantId().equals(merchantId)) {
            throw new BusinessException(400, "无权删除此分类");
        }
        categoryRepo.delete(c);
    }
}
