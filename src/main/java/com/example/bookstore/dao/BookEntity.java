package com.example.bookstore.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="books")
public class BookEntity {
    @Id //первичный ключ
    @GeneratedValue(strategy =  GenerationType.AUTO) //стратегию его генерации
    private Long id;
    private String author;
    private String title;
    private Double price;
}
