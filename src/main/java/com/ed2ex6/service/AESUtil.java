package com.ed2ex6.service;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HexFormat;

public class AESUtil {
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 12;
    private static final int TAG_SIZE = 128;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();


    public static String encrypt(String plaintext, SecretKey secretKey){
        byte[] iv = new byte[IV_SIZE];
        SECURE_RANDOM.nextBytes(iv);

        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }

        GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE, iv);

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }

        byte[] ciphertext = null;

        try {
            ciphertext = cipher.doFinal(
                    plaintext.getBytes(StandardCharsets.UTF_8)
            );
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }

        byte[] result = new byte[iv.length + ciphertext.length];

        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(ciphertext, 0, result, iv.length, ciphertext.length);

        return HexFormat.of().formatHex(result);
    }

    public static String decrypt(String ciphertext, SecretKey secretKey){
        byte[] decoded = HexFormat.of().parseHex(ciphertext);

        byte[] iv = Arrays.copyOfRange(decoded, 0, IV_SIZE);
        byte[] encryptedBytes = Arrays.copyOfRange(decoded, IV_SIZE, decoded.length);

        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }

        GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE, iv);

        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }

        byte[] plaintext = null;

        try {
            plaintext = cipher.doFinal(encryptedBytes);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }

        return new String(plaintext, StandardCharsets.UTF_8);
    }
}
