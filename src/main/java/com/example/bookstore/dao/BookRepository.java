package com.example.bookstore.dao;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<BookEntity, Long> {
    List<BookEntity> findAllByAuthorContaining(String author); //Spring анотация findAll — найти все записи, ByAuthor — параметр обрамляется %, SELECT * FROM books WHERE author LIKE '%Bloch%';
}
