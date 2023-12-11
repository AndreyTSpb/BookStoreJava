package com.example.bookstore.service;

import com.example.bookstore.dao.BookEntity;
import com.example.bookstore.dao.BookRepository;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.mapper.BookToEntityMapper;
import com.example.bookstore.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultBookService implements BookService{
    private final BookRepository bookRepository;
    private final BookToEntityMapper mapper;

    @Override
    public Book getBookById(Long id) {
        BookEntity bookEntity = bookRepository
                .findById(id)
                .orElseThrow(() -> new BookNotFoundException("Книга с идентификатором: " + id + " ненайдена"));
        return mapper.bookEntityToBook(bookEntity);
    }

    @Override
    public List<Book> getAllBooks() {
        Iterable<BookEntity> iterable = bookRepository.findAll();
        ArrayList<Book> books = new ArrayList<>();
        for(BookEntity bookEntity : iterable){
            books.add(mapper.bookEntityToBook(bookEntity));
        }
        return books;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        Iterable<BookEntity> iterable = bookRepository.findAllByAuthorContaining(author);
        ArrayList<Book> books = new ArrayList<>();
        for (BookEntity bookEntity : iterable){
            books.add(mapper.bookEntityToBook(bookEntity));
        }
        return books;
    }

    @Override
    public void addBook(Book book) {
        BookEntity bookEntity = mapper.bookToBookEntity(book);
        bookRepository.save(bookEntity);
    }
}
