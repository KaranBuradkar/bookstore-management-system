package com.karanburadkar.bookstore.repository;

import jakarta.transaction.Transactional;
import com.karanburadkar.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Book b WHERE b.id = ?1")
    int deleteByIdCustom(Long bookId);

    @Query("SELECT b FROM Book b " +
            "LEFT JOIN FETCH b.publisher p " +
            "LEFT JOIN FETCH b.authors a " +
            "LEFT JOIN FETCH b.categories c")
    Page<Book> findBookDetails(Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.id = :authorId")
    List<Book> findByAuthorId(@Param("authorId") Long authorId);

    @Query("SELECT b.publisher.user.id FROM Book b WHERE b.id = ?1")
    Optional<Long> getUserIdById(Long bookId);

    boolean existsByIsbn(String isbn);
}

