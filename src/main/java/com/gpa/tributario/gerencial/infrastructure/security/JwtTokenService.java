package com.gpa.tributario.gerencial.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gpa.tributario.gerencial.entity.Usuario;
import com.gpa.tributario.gerencial.enuns.RoleNameEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class JwtTokenService {

    private static final String SECRET_KEY = "GPABR@dWxqf$mTTKwW$!@#qGr4P"; // Chave secreta utilizada para gerar e verificar o token

    private static final String ISSUER = "pizzurg-api"; // Emissor do token

    @Value("${spring.security.expiration-time}")
    private Integer EXPIRATION_TIME;

    public String generateToken(UserDetailsImpl user) {
        try {
            // Define o algoritmo HMAC SHA256 para criar a assinatura do token passando a chave secreta definida
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withIssuer(ISSUER) // Define o emissor do token
                    .withIssuedAt(creationDate()) // Define a data de emissão do token
                    .withExpiresAt(expirationDate()) // Define a data de expiração do token
                    .withSubject(user.getUsername()) // Define o assunto do token (neste caso, o nome de usuário)
                    .withClaim("Authorities", user.getUser().getRoles().stream().map(Enum::name).toList())
                    .withClaim("Nome", user.getUser().getNome())
                    .withClaim("Email", user.getUser().getEmail())
                    .sign(algorithm); // Assina o token usando o algoritmo especificado


        } catch (JWTCreationException exception){
            throw new JWTCreationException("Erro ao gerar token.", exception);
        }
    }

       public UserDetailsImpl getDataFromToken(String token) {
        try {
            // Define o algoritmo HMAC SHA256 para verificar a assinatura do token passando a chave secreta definida
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            DecodedJWT decode = JWT.require(algorithm)
                    .withIssuer(ISSUER) // Define o emissor do token
                    .build()
                    .verify(token);

            Usuario usuario = new Usuario();

            usuario.setUserName(decode.getSubject());
            usuario.setNome(String.valueOf(decode.getClaim("Nome")));
            usuario.setEmail(String.valueOf(decode.getClaim("Email")));
            usuario.setRoles(decode.getClaim("Authorities").asList(RoleNameEnum.class));

            UserDetailsImpl ud = new UserDetailsImpl(usuario);
            ud.setTimeToExpire(verifyTime(decode.getExpiresAt()));

            return ud;

//            return JWT.require(algorithm)
//                    .withIssuer(ISSUER) // Define o emissor do token
//                    .build()
//                    .verify(token) // Verifica a validade do token
//                    .getSubject();


        } catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token inválido ou expirado.");
        }
    }

    private Instant creationDate() {
        return ZonedDateTime.now().toInstant();
    }

    private Instant expirationDate() {
        return ZonedDateTime.now().plusMinutes(EXPIRATION_TIME).toInstant();
    }

    private long verifyTime(Date date){
        LocalDateTime convertedDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return ZonedDateTime.now().toLocalDateTime().until(convertedDateTime, java.time.temporal.ChronoUnit.SECONDS);
    }

}
