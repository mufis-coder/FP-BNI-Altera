package com.bnifp.mufis.categoryservice.service.impl;

import com.bnifp.mufis.categoryservice.dto.input.CategoryInput;
import com.bnifp.mufis.categoryservice.dto.output.CategoryOutput;
import com.bnifp.mufis.categoryservice.exception.DataNotFoundException;
import com.bnifp.mufis.categoryservice.exception.InputNullException;
import com.bnifp.mufis.categoryservice.model.Category;
import com.bnifp.mufis.categoryservice.repository.CategoryRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class CategoryServiceImplTest {
    private final EasyRandom easyRandom = new EasyRandom();
    private final ModelMapper modelMapper = new ModelMapper();
    private Long id;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;
    @Spy
    private ModelMapper mockModelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        id = easyRandom.nextObject(Long.class);
    }

    @Test
    public void addOne_WillReturnCategoryOutput() throws InputNullException {
        // Given
        CategoryInput input = easyRandom.nextObject(CategoryInput.class);
        Category category = modelMapper.map(input, Category.class);
        when(categoryRepository.save(category)).thenReturn(category);

        // When
        var result = categoryService.addOne(input);

        // Then
        verify(categoryRepository, times(1)).save(category);
        verify(mockModelMapper, times(1)).map(input, Category.class);
        verify(mockModelMapper, times(1)).map(category, CategoryOutput.class);
    }

    @Test
    public void getOne_WillExeption() {
        // Given
        String errMsg = "Category with id: " + id.toString() + " is not Found";
        when(categoryRepository.findById(id)).thenAnswer( invocation -> { throw new DataNotFoundException(errMsg); });

        // When
        Exception thrown = assertThrows(
                Exception.class,
                () -> categoryService.getOne(id),
                "Expected getOne() to throw, but it didn't"
        );

        // Then
        assertTrue(thrown.getMessage().equals(errMsg));
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    public void getOne_WillReturnCategoryOutput() throws DataNotFoundException {
        // Given
        Category category = easyRandom.nextObject(Category.class);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        // When
        var result = categoryService.getOne(category.getId());

        // Then
        verify(categoryRepository, times(1)).findById(category.getId());
        CategoryOutput output = modelMapper.map(category, CategoryOutput.class);
        assertEquals(output, result);
    }


}
