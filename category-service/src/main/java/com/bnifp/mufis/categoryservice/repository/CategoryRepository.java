package com.bnifp.mufis.categoryservice.repository;

import com.bnifp.mufis.categoryservice.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long>  {
}
