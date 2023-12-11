package com.example.bookstore.service;

public interface ClientService {
    void register(String clientId, String clientSecret); //регистрация клиента
    void checkCredentials(String clientId, String clientSecret); //проверка реквизитов для входа (id и pass)
}
