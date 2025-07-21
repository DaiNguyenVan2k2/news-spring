package com.ptit.news.service;

import com.ptit.news.dto.CategoryCreateUpdateDTO;
import com.ptit.news.dto.CategoryDTO;
import com.ptit.news.entity.Category;
import com.ptit.news.exception.ResourceNotFoundException;
import com.ptit.news.exception.ValidationException;
import com.ptit.news.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDTO createCategory(CategoryCreateUpdateDTO createDto) {
        // Kiểm tra xem content đã tồn tại chưa (không phân biệt hoa thường)
        if (categoryRepository.existsByContentAndIsDeletedFalse(createDto.getContent())) {
            throw new ValidationException("Category with content '" + createDto.getContent() + "' already exists.");
        }

        Category category = new Category();
        category.setContent(createDto.getContent());
        category.setIsDeleted(false); // Đảm bảo trạng thái ban đầu là không xóa

        if (createDto.getParentId() != null) {
            Category parentCategory = categoryRepository.findByIdAndIsDeletedFalse(createDto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with ID: " + createDto.getParentId()));
            category.setParent(parentCategory);
        }

        Category savedCategory = categoryRepository.save(category);
        return CategoryDTO.fromEntity(savedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        // Lấy tất cả các category chưa bị xóa
        List<Category> categories = categoryRepository.findAllByIsDeletedFalseOrderByContentAsc();

        // Xây dựng cây phân cấp từ danh sách phẳng
        // Lọc ra các category gốc (parent là null)
        List<Category> rootCategories = categories.stream()
                .filter(category -> category.getParent() == null)
                .collect(Collectors.toList());

        // Chuyển đổi thành DTO và bao gồm các category con
        return rootCategories.stream()
                .map(CategoryDTO::fromEntityWithChildren)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        return CategoryDTO.fromEntityWithChildren(category);
    }

    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryCreateUpdateDTO updateDto) {
        Category existingCategory = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        // Kiểm tra xem content mới có bị trùng với category khác (ngoại trừ chính nó)
        if (!existingCategory.getContent().equalsIgnoreCase(updateDto.getContent()) &&
                categoryRepository.existsByContentAndIsDeletedFalse(updateDto.getContent())) {
            throw new ValidationException("Category with content '" + updateDto.getContent() + "' already exists.");
        }

        existingCategory.setContent(updateDto.getContent());

        // Cập nhật parent category
        if (updateDto.getParentId() != null) {
            // Không cho phép category tự làm parent của chính nó
            if (updateDto.getParentId().equals(id)) {
                throw new ValidationException("Category cannot be its own parent.");
            }
            Category newParent = categoryRepository.findByIdAndIsDeletedFalse(updateDto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with ID: " + updateDto.getParentId()));
            existingCategory.setParent(newParent);
        } else {
            // Nếu parentId là null, đặt category này thành category gốc
            existingCategory.setParent(null);
        }

        Category updatedCategory = categoryRepository.save(existingCategory);
        return CategoryDTO.fromEntity(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category categoryToDelete = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        // Thực hiện soft delete: đánh dấu category và tất cả các category con của nó là isDeleted = true
        markCategoryAndChildrenAsDeleted(categoryToDelete);
    }

    // Helper method để đánh dấu category và tất cả các category con là đã xóa
    private void markCategoryAndChildrenAsDeleted(Category category) {
        if (category == null || category.getIsDeleted()) {
            return;
        }
        category.setIsDeleted(true);
        categoryRepository.save(category);

        if (category.getChildren() != null) {
            for (Category child : category.getChildren()) {
                markCategoryAndChildrenAsDeleted(child);
            }
        }
    }
}