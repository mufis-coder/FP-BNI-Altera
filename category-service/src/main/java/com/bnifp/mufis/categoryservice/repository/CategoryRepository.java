package com.bnifp.mufis.categoryservice.repository;

import com.bnifp.mufis.categoryservice.model.Category;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long>  {
}
