package com.ed2ex6.service;

import com.ed2ex6.model.CryptographyAlgorithm;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class CryptographyService {
    public String encrypt(String plaintext, CryptographyAlgorithm algorithm, SecretKey secretKey) {
        if (algorithm == CryptographyAlgorithm.DES)
            return DESUtil.encrypt(plaintext, secretKey);
        else
            return AESUtil.encrypt(plaintext, secretKey);
    }

    public String decrypt(String ciphertext, CryptographyAlgorithm algorithm, SecretKey secretKey) {
        if (algorithm == CryptographyAlgorithm.DES)
            return DESUtil.decrypt(ciphertext, secretKey);
        else
            return AESUtil.decrypt(ciphertext, secretKey);
    }
}