package com.jango.assignmenttracker.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.Base64;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 31557600000l; //1 week
    public static final String TOKEN_PREFIX = "bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOGIN_URL = "/user/login";


    private static final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS512);
    private static final byte[] secretBytes = secret.getEncoded();
    private static final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);

    public static String getSecret() {
        return base64SecretBytes;
    }
}
