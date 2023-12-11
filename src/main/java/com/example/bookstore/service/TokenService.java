package com.example.bookstore.service;

public interface TokenService {
    boolean  checkToken(String toke);

    String generateToken(String clientId);
}
