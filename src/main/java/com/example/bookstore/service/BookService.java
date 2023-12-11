package com.example.bookstore.service;

import com.example.bookstore.model.Book;

import java.util.List;

public interface BookService {
    Book getBookById(Long id);// получить книгу по id
    List<Book> getAllBooks();// получить список всех книг
    List<Book> findByAuthor(String author); //Поиск книги по автору(вернет все с указанным автором)
    void addBook(Book book);// добавить книгу
}
