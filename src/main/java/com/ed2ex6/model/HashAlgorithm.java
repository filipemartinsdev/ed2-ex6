package com.ed2ex6.model;

public enum HashAlgorithm {
    MD5("MD5"),
    SHA_256("SHA-256"),
    SHA_512("SHA-512"),
    SHA3_256("SHA3-256"),
    SHA3_512("SHA3-512");

    public final String label;

    HashAlgorithm(String label) {
        this.label = label;
    }
}
