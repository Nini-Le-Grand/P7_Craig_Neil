package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return an {@link Optional} containing the found user or empty if no user was found
     */
    Optional<User> findByUsername(String username);
}
