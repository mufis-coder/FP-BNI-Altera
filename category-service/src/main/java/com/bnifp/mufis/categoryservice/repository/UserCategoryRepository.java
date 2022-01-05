package com.bnifp.mufis.categoryservice.repository;

import com.bnifp.mufis.categoryservice.model.UserCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCategoryRepository extends CrudRepository<UserCategory, Long> {
    List<UserCategory> findByUserId(Long userId);

    UserCategory findByUserIdAndCategoryId(Long userId, Long categoryId);
}
