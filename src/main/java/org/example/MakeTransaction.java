package org.example;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class MakeTransaction {
    private Wallet walletA;
    private Wallet walletB;


    public MakeTransaction(){
        Security.addProvider(new BouncyCastleProvider());

        walletA = new Wallet();
        walletB = new Wallet();

        System.out.println("Wallet A public key - " + Helper.getStringFromKey(walletA.getPublicKey()));
        System.out.println("Wallet A private key - " + Helper.getStringFromKey(walletA.getPrivateKey()));
        System.out.println("Wallet B public key - " + Helper.getStringFromKey(walletA.getPublicKey()));
        System.out.println("Wallet B private key - " + Helper.getStringFromKey(walletA.getPrivateKey()));

        Transaction transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), 5, null);
        transaction.generateSignature(walletA.getPrivateKey());

        System.out.println("Is signature verified - " + transaction.verifySignature());
    }
}
