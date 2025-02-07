package org.example;

import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

    public Wallet(){
        generateKeyPair();
    }

    /**
     * Using Elliptic-curve cryptography to generate key pair values
     * Read https://en.wikipedia.org/wiki/Elliptic-curve_cryptography
     */
    private void generateKeyPair(){
        try {
            KeyPairGenerator kpGenerator = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            kpGenerator.initialize(ecSpec, random);
            KeyPair kp = kpGenerator.generateKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();

//            // Print public key points
//            ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
//            ECPoint point = ecPublicKey.getW();
//            System.out.println("Public Key (X coordinate): " + point.getAffineX().toString(16));
//            System.out.println("Public Key (Y coordinate): " + point.getAffineY().toString(16));
//
//            // Print private key

//            ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
//            System.out.println("Private Key (secret value): " + ecPrivateKey.getS().toString(16));
        } catch (InvalidAlgorithmParameterException | NoSuchProviderException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Exception while generating the key pair - " + e.getMessage());
        }
    }

    //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
    public float getBalance(){
        float total = 0;

        for (Map.Entry<String, TransactionOutput> item : CryptoCoin.UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            if (UTXO.isMine(publicKey)){
                UTXOs.put(UTXO.getId(), UTXO); //add it to our list of unspent transactions.
                total += UTXO.getValue();
            }
        }
        return total;
    }

    //Generates and returns a new transaction from this wallet
    public Transaction sendCoins(PublicKey _recipient, float value){
        if(getBalance() < value){
            System.out.println("Insufficient balance");
            return null;
        }

        ArrayList<TransactionInput> inputs = new ArrayList<>();
        float total = 0;
        for(Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            total += UTXO.getValue();
            inputs.add(new TransactionInput(UTXO.getId()));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
        newTransaction.generateSignature(privateKey);

        for(TransactionInput input : inputs){
            UTXOs.remove(input.getTransactionId());
        }
        return newTransaction;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
