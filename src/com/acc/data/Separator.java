package com.acc.data;

/**
 * Created by Rumpy on 15-01-2015.
 */
public class Separator implements Token {
    private String token;

    public Separator(String token) {
        this.token = token;
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.SEPARATOR;
    }
}