package com.ed2ex6.service;

import com.ed2ex6.model.CryptographyAlgorithm;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class KeyUtils {
    public static SecretKey random(CryptographyAlgorithm algorithm){
        try {
            return KeyGenerator.getInstance(algorithm.name()).generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHex(SecretKey secretKey){
        return HexFormat.of().formatHex(secretKey.getEncoded());
    }

    public static SecretKey fromHex(String hex, CryptographyAlgorithm algorithm){
        byte[] bytes = HexFormat.of().parseHex(hex);
        return new SecretKeySpec(bytes, algorithm.name());
    }
}
