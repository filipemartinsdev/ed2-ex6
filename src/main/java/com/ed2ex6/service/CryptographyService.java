package com.ed2ex6.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class CryptographyService {
    public String toHash(String text, String algorithm){
        byte[] hashBytes = getMessageDigestInstance(algorithm)
                .digest(text.getBytes());

        return HexFormat
                .of()
                .formatHex(hashBytes);
    }

    private MessageDigest getMessageDigestInstance(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
