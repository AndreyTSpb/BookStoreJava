package com.example.bookstore;

import com.example.bookstore.exception.RegistrationException;
import com.example.bookstore.model.ErrorResponse;
import com.example.bookstore.model.TokenResponse;
import com.example.bookstore.model.User;
import com.example.bookstore.service.ClientService;
import com.example.bookstore.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;

@RestController
@RequestMapping("/sing_in")
@RequiredArgsConstructor
public class AuthenticController {
    private final ClientService clientService;
    private final TokenService tokenService;

    /**
     * Регистрация пользователя,
     * по идентификатору и ключу.
     * POST url = /sing_in
     * JSON { "clientId": "userId", "clientSecret": "pass" }
     * @param user
     * @return
     */
    @PostMapping
    public ResponseEntity<String> register(@RequestBody User user){
        clientService.register(
                user.getClientId(),
                user.getClientSecret()
        );
        return ResponseEntity.ok("Зарегистрирован");
    }


    /**
     * Генерация токена
     * для прошедшего проверку
     * POST url = /sing_in/token
     * JSON { "clientId": "userId", "clientSecret": "pass" }
     * @param user
     * @return
     */
    @PostMapping("/token")
    public TokenResponse getToken(@RequestBody User user){
        clientService.checkCredentials(
            user.getClientId(),
            user.getClientSecret()
        );
        return new TokenResponse(tokenService.generateToken(user.getClientId()));
    }

    @ExceptionHandler({RegistrationException.class, LoginException.class})
    public ResponseEntity<ErrorResponse> handleUserRegistrationException(RuntimeException ex){
        return  ResponseEntity.badRequest()
                .body(new ErrorResponse(ex.getMessage()));
    }
}
