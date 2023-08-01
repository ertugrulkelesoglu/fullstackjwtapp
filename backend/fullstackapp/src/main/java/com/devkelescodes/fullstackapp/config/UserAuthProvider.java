package com.devkelescodes.fullstackapp.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.devkelescodes.fullstackapp.dto.UserDto;
import com.devkelescodes.fullstackapp.entity.User;
import com.devkelescodes.fullstackapp.exceptions.AppException;
import com.devkelescodes.fullstackapp.mappers.UserMapper;
import com.devkelescodes.fullstackapp.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Component
public class UserAuthProvider {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

     @Value("${security.jwt.token.secret-key:secret-key}")
     private String secretKey;

     protected void init() {
         secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
     }

    public String createToken(UserDto userDto) {
         Date now = new Date();
         Date validity = new Date( now.getTime() + 3_600_000);

         return JWT.create()
                 .withIssuer(userDto.getLogin())
                 .withIssuedAt(now)
                 .withExpiresAt(validity)
                 .withClaim("firstName", userDto.getFirstName())
                 .withClaim("lastName", userDto.getLastName())
                 .sign(Algorithm.HMAC256(secretKey));
    }

    public Authentication validateToken(String token) {
         Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decoded = verifier.verify(token);

        UserDto user = new UserDto(decoded.getClaim("firstName").asString(),
                decoded.getClaim("lastName").asString(),
                decoded.getIssuer());

        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());


    }

    public Authentication validateTokenStrongly(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decoded = verifier.verify(token);

        User user = userRepository.findByLogin(decoded.getIssuer())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        return new UsernamePasswordAuthenticationToken(userMapper.userToUserDto(user), null, Collections.emptyList());

    }


}
