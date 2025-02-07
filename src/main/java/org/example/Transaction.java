package org.example;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

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

    public void generateSignature(PrivateKey privateKey){
        String data = Helper.getStringFromKey(sender) + Helper.getStringFromKey(recipient) + Float.toString(value);
        signature = Helper.applyEcdsaSignature(privateKey, data);
    }

    public boolean verifySignature(){
        String data = Helper.getStringFromKey(sender) + Helper.getStringFromKey(recipient) + Float.toString(value);
        return Helper.verifyEcdsaSignature(sender, data, signature);
    }

    public boolean processTransaction(){
        if(!verifySignature()){
            System.out.println("Transaction signature failed to verify.");
            return false;
        }

        // gather transaction inputs to check if they are unspent
        for (TransactionInput input : inputs){
            input.UTXO = (CryptoCoin.UTXOs.get(input.getTransactionId()));
        }

        //check if transaction is valid
        if(getInputValue() < CryptoCoin.minimumTransaction){
            System.out.println("Transaction input too small");
            System.out.println("Minimum " + CryptoCoin.minimumTransaction + " of value of transaction required.");
            return false;
        }

        //generate transaction outputs
        float leftover = getInputValue() - value;
        transactionId = calculateHash_ForTransactionId();
        outputs.add(new TransactionOutput(this.recipient, value, transactionId));
        outputs.add(new TransactionOutput(this.sender, leftover, transactionId));

        //add outputs to the unspent list
        for (TransactionOutput output : outputs){
            CryptoCoin.UTXOs.put(output.getId(), output);
        }

        //remove transaction inputs from UTXO lists as spent:
        for (TransactionInput input : inputs){
            if(input.UTXO == null) continue;
            CryptoCoin.UTXOs.remove(input.UTXO.getId());
        }

        return true;
    }

    private float getInputValue() {
        float total = 0;
        for (TransactionInput input : inputs){
            if(input.UTXO == null) continue;;
            total += input.UTXO.getValue();
        }
        return total;
    }

    private float getOutputValue(){
        float total = 0;
        for (TransactionOutput output : outputs){
            total += output.getValue();
        }
        return total;
    }
}
