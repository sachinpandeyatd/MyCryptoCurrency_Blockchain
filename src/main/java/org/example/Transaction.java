package org.example;

import java.security.*;
import java.util.ArrayList;

public class Transaction {
    private String transactionId;
    private PublicKey sender;
    private PublicKey recipient;
    private float value;
    private byte[] signature;
    private ArrayList<TransactionInput> inputs = new ArrayList<>();
    private ArrayList<TransactionOutput> outputs = new ArrayList<>();
    private static int sequence = 0;

    public Transaction(PublicKey sender, PublicKey recipient, float value, ArrayList<TransactionInput> inputs) {
        this.sender = sender;
        this.recipient = recipient;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash_ForTransactionId(){
        sequence++;
        return Helper.hashToSHA_256(Helper.getStringFromKey(sender) +
                Helper.getStringFromKey(recipient) + Float.toString(sequence));
    }

    private void generateSignature(PrivateKey privateKey){
        String data = Helper.getStringFromKey(sender) + Helper.getStringFromKey(recipient) + Float.toString(value);
        signature = Helper.applyEcdsaSignature(privateKey, data);
    }

    private boolean verifySignature(){
        String data = Helper.getStringFromKey(sender) + Helper.getStringFromKey(recipient) + Float.toString(value);
        return Helper.verifyEcdsaSignature(sender, data, signature);
    }
}
