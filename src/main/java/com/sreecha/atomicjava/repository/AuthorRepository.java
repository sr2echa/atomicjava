package com.sreecha.atomicjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sreecha.atomicjava.model.Author;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByNameContainingIgnoreCase(String name);
    Optional<Author> findByName(String name);
}