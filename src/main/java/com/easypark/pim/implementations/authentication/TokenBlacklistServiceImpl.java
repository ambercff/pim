package com.easypark.pim.implementations.authentication;

import com.easypark.pim.entities.TokenBlacklist;
import com.easypark.pim.repositories.TokenBlacklistRepository;
import com.easypark.pim.services.authentication.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Override
    public void blacklistToken(String token) {
        TokenBlacklist blacklistedToken = new TokenBlacklist();
        blacklistedToken.setToken(token);
        tokenBlacklistRepository.save(blacklistedToken);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }
}
