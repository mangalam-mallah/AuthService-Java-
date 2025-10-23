package org.example.service;

import org.example.entities.RefreshToken;
import org.example.entities.UserInfo;
import org.example.repository.RefreshTokenRepo;
import org.example.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired RefreshTokenRepo refreshTokenRepo;

    @Autowired UserRepo userRepo;

    public RefreshToken createRefreshToken(String username){
        UserInfo userInfoExtract = userRepo.findByUsername(username);
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userInfoExtract)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        return refreshTokenRepo.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepo.delete(token);
            throw new RuntimeException(token.getToken() + "Refresh token is expired. Please make a new login");
        }
        return token;
    }

    public Optional<RefreshToken> findBytoken(String token){
        return refreshTokenRepo.findByToken(token);
    }



}
