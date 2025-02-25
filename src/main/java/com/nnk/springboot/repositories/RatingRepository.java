package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Rating} entities.
 */
public interface RatingRepository extends JpaRepository<Rating, Integer> {

}
