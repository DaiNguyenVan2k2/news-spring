package com.ptit.news.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ptit.news.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("SELECT t FROM Token t WHERE t.user.email = ?1 AND t.isSignOut = false")
    List<Token> findTokensValidOfUser(String email);

    Optional<Token> findByCode(String code);

}
