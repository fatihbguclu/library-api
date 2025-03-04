package com.ft.library.repository;

import com.ft.library.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBookById(Long id);

    Optional<Book> findBookByIsbn(String isbn);

}
