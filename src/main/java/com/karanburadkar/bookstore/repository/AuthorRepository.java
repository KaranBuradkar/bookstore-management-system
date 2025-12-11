package com.karanburadkar.bookstore.repository;

import com.karanburadkar.bookstore.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a WHERE a.id = ?1")
    Page<Author> findAuthorsByBookId(Long bookId, Pageable pageable);

    boolean existsByName(String name);
}
