package com.example.bookstore.service;

import com.example.bookstore.dao.ClientEntity;
import com.example.bookstore.dao.ClientRepository;
import com.example.bookstore.exception.LoginException;
import com.example.bookstore.exception.RegistrationException;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 */
@Service
@RequiredArgsConstructor
public class DefaultClientService implements ClientService{
    private final ClientRepository userRepository;

    /**
     * Регистрация
     * @param clientId
     * @param clientSecret
     */
    @Override
    public void register(String clientId, String clientSecret) {
        //проверка сушествует ли клиент с таким идентификатором
        if(userRepository.findById(clientId).isPresent()){
            throw new RegistrationException("Клиент с идентификатором " + clientId + " уже зарегистрирован");
        }
        //генерим хеш из пароля
        String hash = BCrypt.hashpw(clientSecret, BCrypt.gensalt());
        //добавляем в БД
        userRepository.save(new ClientEntity(clientId, hash));
    }

    /**
     * Авторизация
     * @param clientId
     * @param clientSecret
     */
    @Override
    public void checkCredentials(String clientId, String clientSecret){
        Optional<ClientEntity> optionalClientEntity = userRepository.findById(clientId);
        if(optionalClientEntity.isEmpty()){
            throw new LoginException("Клиент с иденификатором " + clientId + " не найден");
        }
        ClientEntity clientEntity = optionalClientEntity.get();
        if(!BCrypt.checkpw(clientSecret, clientEntity.getHash())){
            throw new LoginException("Пароль не корректен");
        }
    }
}
