package org.example;

public class TransactionInput {
    private String transactionId;
    private TransactionOutput UTXO;

    public TransactionInput(String transactionId){
        this.transactionId = transactionId;
    }
}
