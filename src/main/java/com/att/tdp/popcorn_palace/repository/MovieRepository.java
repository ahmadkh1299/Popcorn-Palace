package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for Movie entity.
 * Extends JpaRepository to provide built-in CRUD operations.
 */
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * Finds a movie by its title.
     *
     * @param title The movie title
     * @return Optional containing the movie if it exists
     */
    Optional<Movie> findByTitle(String title);
}
