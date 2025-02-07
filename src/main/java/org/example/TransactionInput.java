package org.example;

public class TransactionInput {
    private String transactionId;
    public TransactionOutput UTXO;

    public TransactionInput(String transactionId){
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
