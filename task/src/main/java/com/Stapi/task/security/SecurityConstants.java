package com.Stapi.task.security;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class SecurityConstants {

    public static final SecretKey KEY = Keys.hmacShaKeyFor("YourSuperSecretKeyYourSuperSecretKey".getBytes());
    public static final Integer EXPIRY_TIME = 300000;

}
