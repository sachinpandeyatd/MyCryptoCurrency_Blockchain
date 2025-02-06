package org.example;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Wallet(){
        try {
            generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Exception while generating the key pair - " + e.getMessage());
        }
    }

    /**
     * Using Elliptic-curve cryptography to generate key pair values
     * Read https://en.wikipedia.org/wiki/Elliptic-curve_cryptography
     */
    private void generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator kpGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
        kpGenerator.initialize(ecSpec, random);

        KeyPair kp = kpGenerator.generateKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();
    }
}
