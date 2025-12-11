package com.karanburadkar.bookstore.repository;

import com.karanburadkar.bookstore.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
    Optional<BookCategory> findByName(String name);

    @Query("SELECT c FROM BookCategory c WHERE c.name in :categoryNames")
    List<BookCategory> findByNameIn(@Param("categoryNames") Set<String> categoryNames);
}
