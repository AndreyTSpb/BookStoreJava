package com.example.bookstore.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Slf4j
public class DefaultTokenService implements TokenService {
    @Value("${auth.jwt.secret}")
    private String secretKey;

    /**
     * Проверка токена
     * @param token
     * @return
     */
    @Override
    public boolean checkToken(String token) {
        //Берем данные secretKey из настроек application.properties и делаем хэш
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        //Объект для верификации
        JWTVerifier verifier = JWT.require(algorithm).build();

        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            //Проверка на соответствие издателю ключа
            if (!decodedJWT.getIssuer().equals("auth-service")) {
                log.error("Издатель токена неверный");
                return false;
            }
            //проверка на соответствия для кого ключ выпущен
            if (!decodedJWT.getAudience().contains("bookstore")) {
                log.error("Токен выпущен не для этого слушателя");
                return false;
            }
        } catch (JWTVerificationException e) {
            //если токен кривой
            log.error("Токен неверный: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public String generateToken(String clientId) {
        //хешируем secretKey
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        Instant now = Instant.now(); // текущее время
        Instant exp = now.plus(5, ChronoUnit.MINUTES); //срок годности для токена

        /**
         * Формируем токен
         * 1) Указываем какой сервис его выпустил (издатель)
         * 2) Указываем для какого сервиса он выпущен
         * 3) Указываем идентификатор клиента
         * 4) Дата выпуска
         * 5) Дата годности
         * 6) Алгоритм шифрования
         */
        return JWT.create()
                .withIssuer("auth-service")
                .withAudience("bookstore")
                .withSubject(clientId)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(algorithm);
    }
}
