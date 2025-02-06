package org.example;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Wallet(){
        generateKeyPair();
    }

    /**
     * Using Elliptic-curve cryptography to generate key pair values
     * Read https://en.wikipedia.org/wiki/Elliptic-curve_cryptography
     */
    private void generateKeyPair(){
        Security.addProvider(new BouncyCastleProvider());
        try {
            KeyPairGenerator kpGenerator = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            kpGenerator.initialize(ecSpec, random);
            KeyPair kp = kpGenerator.generateKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();

            // Print public key points
            ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
            ECPoint point = ecPublicKey.getW();
            System.out.println("Public Key (X coordinate): " + point.getAffineX().toString(16));
            System.out.println("Public Key (Y coordinate): " + point.getAffineY().toString(16));

            // Print private key
            ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
            System.out.println("Private Key (secret value): " + ecPrivateKey.getS().toString(16));
        } catch (InvalidAlgorithmParameterException | NoSuchProviderException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Exception while generating the key pair - " + e.getMessage());
        }
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
