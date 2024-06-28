package com.chainsys.examease.encrypt;

import java.security.KeyPairGenerator;

public class KeyPair {
    public static void main(String[] args) throws Exception {
        java.security.KeyPair keyPair = generateKeyPair();
        keyPair.getPublic();
        keyPair.getPrivate();

    }

    public static java.security.KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

}
