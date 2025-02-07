package org.example;

import java.security.PublicKey;

public class TransactionOutput {
    private String id;
    private PublicKey recipient;
    private float value;
    private String parentTransactionId;

    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId){
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        id = Helper.hashToSHA_256(Helper.getStringFromKey(recipient) +
                value + parentTransactionId);
    }

    public boolean isMine(PublicKey publicKey){
        return (publicKey == recipient);
    }
}
