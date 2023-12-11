package com.example.bookstore;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.BookRequest;
import com.example.bookstore.mapper.BookToDtoMapper;
import com.example.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookToDtoMapper bookMapper;

    /**
     * Получить книгу по ее идентификатору
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id){
        return bookService.getBookById(id);
    }

    /**
     * Получить список книг
     * если передан автор, то выбрать по автору,
     * иначе все
     * @return - писок в JSON
     */
    @GetMapping
    public List<Book> getAllBooks(@RequestParam(required = false) String author){
        this.genKey();
        if (author != null){
            return  bookService.findByAuthor(author);
        }
        return bookService.getAllBooks();
    }

    /**
     * Добавить книгу в таблицу
     * @param bookRequest - JSON
     */
    @PostMapping
    public void addBook(@RequestBody BookRequest bookRequest){
        bookService.addBook(bookMapper.AddBookRequestToBook(bookRequest));
    }

    public void genKey(){
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        String secretKey = new BigInteger(1, bytes).toString(16);
        System.out.println(secretKey);
    }
}
